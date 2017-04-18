package competition.subsystems.drive.commands;

import java.util.function.DoubleSupplier;

import com.google.inject.Inject;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;

public class DriveForDistanceAtHeadingCommand extends BaseDriveCommand {
    protected final DoubleProperty headingThreshToAllowTranslation;
    protected final PoseSubsystem poseSubsystem;
    protected ContiguousHeading targetHeading;
    protected double targetDistance;
    protected double rampDistance;

    protected DoubleSupplier targetHeadingSupplier;
    protected DoubleSupplier targetDistanceSupplier;
    protected DoubleSupplier rampDistanceSupplier;
    
    private final PIDManager headingDrivePid;
    private final PIDManager distancePid;
    private final PIDManager alignmentPid;
    
    private XYPair initialPosition = null;
    
    @Inject
    public DriveForDistanceAtHeadingCommand(
            DriveSubsystem driveSubsystem, 
            XPropertyManager propMan,
            PoseSubsystem pose,
            RobotAssertionManager assertionManager,
            PIDFactory pidFactory)
    {
        super(driveSubsystem);
        this.poseSubsystem = pose;
        headingThreshToAllowTranslation = propMan.createPersistentProperty("Drive at heading threshold for translation", 5);
        
        headingDrivePid = pidFactory.createPIDManager("Drive at heading rotation", 1 / 80d, 0, 0, 0, 0.75, -0.75, 4, 3d / 20, 0);
        
        this.distancePid = pidFactory.createPIDManager("Drive at heading position", 0.1, 0, 0, 0, 0.5, -0.5, 3, 1, 0.5);
        this.alignmentPid = pidFactory.createPIDManager("Drive at heading alignment", 1/24d, 0, 0, 0, 0.5, -0.5);
    }

    public void setTargetHeading(double heading) {
        targetHeadingSupplier = () -> heading;
    }
    
    public void setTargetHeadingProp(DoubleProperty heading){
        targetHeadingSupplier = () -> heading.get();
    }
    
    public void setTargetHeadingSupplier(DoubleSupplier headingSupplier) {
        targetHeadingSupplier = headingSupplier;
    }

    public void setTargetDistance(double distance) {
        targetDistanceSupplier = () -> distance;
    }
    
    public void setTargetDistanceProp(DoubleProperty distance){
        targetDistanceSupplier = () -> distance.get();
    }
    
    public void setTargetDistanceSupplier(DoubleSupplier distanceSupplier) {
        targetHeadingSupplier = distanceSupplier;
    }

    public void setRampDistance(double rampDistance) {
        rampDistanceSupplier = () -> rampDistance;
    }

    public void setRampDistanceProp(DoubleProperty rampDistance) {
        rampDistanceSupplier = () -> rampDistance.get();
    }
    
    public void overrideTranslationSpeedConstraints(Double min, Double max) {
        distancePid.overrideOutputConstraints(min, max);
    }
    
    public void reset(){
        log.info("Resetting PID");
        headingDrivePid.reset();
        distancePid.reset();
    }
    
    @Override
    public void initialize() {
        if(targetHeadingSupplier == null) {
            log.error("DriveForDistanceAtHeading initialized without a heading setpoint!");
            targetHeading = null;
            return;
        }
        
        if(targetDistanceSupplier == null) {
            log.error("DriveForDistanceAtHeading initialized without a distance setpoint!");
            targetDistance = 0;
            return;
        }
        
        if(rampDistanceSupplier == null) {
            rampDistance = Double.NaN;
        }

        targetHeading = new ContiguousHeading(targetHeadingSupplier.getAsDouble());
        targetDistance = targetDistanceSupplier.getAsDouble();
        rampDistance = rampDistanceSupplier.getAsDouble();
        
        initialPosition = poseSubsystem.getFieldOrientedTotalDistanceTraveled();
        
        log.info("Initializing DriveForDistanceAtHeading with heading " + targetHeading + " and distance " + targetDistance);
        reset();
    }

    @Override
    public void execute() {
        if(targetHeading != null) {
            double errorInDegrees = poseSubsystem.getCurrentHeading().difference(targetHeading);
            double rotationalPower = headingDrivePid.calculate(0, errorInDegrees);
            
            XYPair targetRelativePositionalError = poseSubsystem.getFieldOrientedTotalDistanceTraveled()
                    .add(initialPosition.clone().scale(-1))
                    .rotate(90 - targetHeading.getValue());
            
            double translationPowerFactor = Math.max(
                    headingThreshToAllowTranslation.get() - Math.abs(errorInDegrees), 0) / headingThreshToAllowTranslation.get();
            
            if(rampDistance > 0 && Double.isFinite(rampDistance)) {
                translationPowerFactor *= Math.min(
                    initialPosition.getDistanceToPoint(poseSubsystem.getFieldOrientedTotalDistanceTraveled()) / rampDistance, 1);
            }
            
            double translationPower = distancePid.calculate(targetDistance, targetRelativePositionalError.y) * translationPowerFactor;
            
            // Using the translation power as a multiplication factor ensures that we only align when we are moving toward the goal
            double alignmentPower = alignmentPid.calculate(targetRelativePositionalError.x, 0) * Math.abs(translationPower);
            
            driveSubsystem.tankDrivePowerMode(translationPower + rotationalPower + alignmentPower, translationPower - rotationalPower - alignmentPower);
        }
    }
    
    @Override
    public boolean isFinished() {
        return targetHeading != null && headingDrivePid.isOnTarget() && distancePid.isOnTarget();
    }
    
    @Override 
    public void end(){
        log.info("Ending. Current heading is " + poseSubsystem.getCurrentHeading() + " and target heading was " 
                + targetHeading);
    }
}