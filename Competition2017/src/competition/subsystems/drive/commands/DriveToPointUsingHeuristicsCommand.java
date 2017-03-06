package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveToPointUsingHeuristicsCommand extends BaseDriveCommand {

    private final PoseSubsystem poseSubsystem;
    private FieldPose goalPoint;
    
    private final PIDManager alignToFinalHeadingPid;
    private final PIDManager headTowardsLinePid;
    private final PIDManager destinationPid;
    
    private final DoubleProperty finalHeadingEffectDistance;
    private final DoubleProperty driveToLineEffectDistance;
    
    private boolean deltaMode = false;
    private double xDeltaInches;
    private double yDeltaInches;
    private double deltaHeading;
    
    @Inject
    public DriveToPointUsingHeuristicsCommand(
            DriveSubsystem driveSubsystem,
            PoseSubsystem poseSubsystem,
            PIDFactory pidFactory,
            XPropertyManager propMan) {
        super(driveSubsystem);
        this.poseSubsystem = poseSubsystem;
        
        alignToFinalHeadingPid = pidFactory.createPIDManager("Align to final heading", .1, 0, 0, 0, 1, -1, 2, 2, 1);
        headTowardsLinePid = pidFactory.createPIDManager("Head towards line", .1, 0, 0, 0, 1, -1, 2, 2, 1);
        destinationPid = pidFactory.createPIDManager("DriveHeuristics drive to destination", 0.1, 0, 0, 0, 1, -1, 3, 1, 0.5);
        
        finalHeadingEffectDistance = propMan.createPersistentProperty("DriveHeuristics heading effect distance", 12.0);
        driveToLineEffectDistance = propMan.createPersistentProperty("DriveHeuristics line effect distance", 12.0);
    }
    
    public void setDeltaBasedTravel(double xInches, double yInches, double deltaHeading) {
        this.xDeltaInches = xInches;
        this.yDeltaInches = yInches;
        this.deltaHeading = deltaHeading;
        
        deltaMode = true;
    }
    
    private void initializeDeltaTravel() {
        FieldPose currentPoint = poseSubsystem.getCurrentFieldPose();
        
        // for testing, let's just go ahead a few feet while turning left 90 degrees
        double currentHeading = currentPoint.getHeading().getValue();
        
        // x and y to add
        double yDelta = (Math.sin(Math.toRadians(currentHeading)) * yDeltaInches) + (Math.sin(Math.toRadians(currentHeading+90)) * yDeltaInches);
        double xDelta = (Math.cos(Math.toRadians(currentHeading)) * xDeltaInches) + (Math.cos(Math.toRadians(currentHeading+90)) * xDeltaInches);
        
        goalPoint = new FieldPose(
                new XYPair(
                        currentPoint.getPoint().x + xDelta,
                        currentPoint.getPoint().y + yDelta),
                new ContiguousHeading(currentHeading + deltaHeading));
        
        deltaMode = true;
    }
    
    public void setAbsoluteGoal(double xPosition, double yPosition, double finalHeading) {
        goalPoint = new FieldPose(
                new XYPair(xPosition, yPosition),
                new ContiguousHeading(finalHeading));
        
        deltaMode = false;
    }

    @Override
    public void initialize() {
        // If we're running this command in delta mode, that means each time this command is called
        // it should perform a drive operation relative to its current position.
        
        // On the other hand, if an absolute position was set, the robot should always go to that position,
        // so we don't want to change the goal point.
        if (deltaMode) {
            initializeDeltaTravel();
        }
    }

    /**
     * There are three "forces" at work for this approach.
     * 1) Align to the goal heading. This force grows the closer we are to the line of travel
     * 2) Align towards the line of travel. This force grows the further we are from the line of travel
     * 3) Drive towards the final point. This is a constant force.
     * 
     * Each of these is basically a PID (optionally combined with a scaling factor)
     * 
     * There are some small quirks as well:
     *  If the final point is "behind" the robot, the robot should drive backwards, not forwards. This 
     *  means that force (2) can sometimes use a heading towards the line + 180 degrees.
     */
    @Override
    public void execute() {     
        if (goalPoint == null) {
            // if no goal was ever set, this shouldn't do anything.
            return;
        }
        
        FieldPose currentPose = poseSubsystem.getCurrentFieldPose();
        
        double distanceToLine = goalPoint.getDistanceToLineFromPoint(currentPose.getPoint());
        double yDisplacement = goalPoint.getPoseRelativeDisplacement(currentPose).y;;
        
        // once we are close, this begins to get important
        double finalHeadingScalingFactor = finalHeadingEffectDistance.get() / (finalHeadingEffectDistance.get() + distanceToLine);
        finalHeadingScalingFactor = finalHeadingScalingFactor > 1 ? 1: finalHeadingScalingFactor;
        
        double alignTowardsLineScalingFactor = distanceToLine / driveToLineEffectDistance.get();
        alignTowardsLineScalingFactor = alignTowardsLineScalingFactor > 1 ? 1 : alignTowardsLineScalingFactor;
        
        ContiguousHeading alignTowardsLineHeading = goalPoint.getPerpendicularHeadingTowardsPoint(currentPose);
        double alignTowardsLineError = currentPose.getHeading().difference(alignTowardsLineHeading);  
        
        double finalHeadingError = currentPose.getHeading().difference(goalPoint.getHeading());
        
        // so now we have both scaling factors, our final heading, and our "go to line" heading.
        // now, for the PIDs.
        
        double yForce = destinationPid.calculate(yDisplacement, 0);
        double unscaledAlignToFinalHeadingForce = alignToFinalHeadingPid.calculate(finalHeadingError, 0);
        double unscaledHeadTowardsLineForce = headTowardsLinePid.calculate(alignTowardsLineError, 0);
        
        double scaledFinalHeadingForce = unscaledAlignToFinalHeadingForce * finalHeadingScalingFactor;
        double scaledHeadTowardsLineForce = unscaledHeadTowardsLineForce * alignTowardsLineScalingFactor;
        
        double leftPower = yForce - scaledFinalHeadingForce - scaledHeadTowardsLineForce;
        double rightPower = yForce + scaledFinalHeadingForce + scaledHeadTowardsLineForce;
        
        driveSubsystem.tankDrivePowerMode(leftPower, rightPower);
    }
    
    @Override
    public boolean isFinished() {
        if (goalPoint == null) {
            // If no goal point was set, this should just return immediately.
            log.warn("DriveToPointUsingHeuristics was started, but no goal point was set. "
                    + "Command cannot continue.");
            return true;
        }
        
        return destinationPid.isOnTarget() && alignToFinalHeadingPid.isOnTarget();
    }

}
