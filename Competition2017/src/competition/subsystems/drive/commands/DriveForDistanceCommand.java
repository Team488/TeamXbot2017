package competition.subsystems.drive.commands;

import com.google.inject.Inject;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDManager;
import xbot.common.math.PIDFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;

public class DriveForDistanceCommand extends BaseDriveCommand {
    
    private final PIDManager travelManager;
    private final PoseSubsystem poseSubsystem;
    
    private final DoubleProperty onTargetCountThresholdProp;
    private final DoubleProperty distanceToleranceInches;
    
    private DoubleProperty deltaDistanceProp;
    private double deltaDistance;
    
    private double targetDistance;

    private double previousPositionInches;
    
    private final PIDManager headingDrivePid;
    private ContiguousHeading targetHeading;

    private  int onTargetCount = 0;
    
    @Inject
    public DriveForDistanceCommand(
            DriveSubsystem driveSubsystem,
            XPropertyManager propManager,
            RobotAssertionManager assertionManager,
            PIDFactory pidFactory,
            PoseSubsystem pose) {
        super(driveSubsystem);
        
        this.poseSubsystem = pose;
        this.requires(driveSubsystem);
        this.travelManager = pidFactory.createPIDManager("Drive to position", 0.0775, 0, 0.4, 0.5, -0.5);

        headingDrivePid = pidFactory.createPIDManager("Heading module", 10, 0, 0);
        targetHeading = new ContiguousHeading();
        onTargetCountThresholdProp = propManager.createPersistentProperty("DrvToPos min stabilization loop count", 3);
        distanceToleranceInches = propManager.createPersistentProperty("Distance tolerance inches", 1.0);
    }
    
    /**
     * Sets the target distance
     * @param deltaDistance the target in inches
     */
    public void setDeltaDistance(double deltaDistance) {
        this.deltaDistance = deltaDistance;
    }
    
    /**
     * Sets the target distance with a DoubleProperty
     * @param deltaDistanceProp the DoubleProperty for the target
     */
    public void setDeltaDistance(DoubleProperty deltaDistanceProp) {
        this.deltaDistanceProp = deltaDistanceProp;
    }
    
    @Override
    public void initialize() {
        onTargetCount = 0;
        previousPositionInches = getYDistance();
        
        targetHeading = poseSubsystem.getCurrentHeading();
        
        if (deltaDistanceProp != null) {
            this.targetDistance = getYDistance() + deltaDistanceProp.get();
        } else {
            this.targetDistance = getYDistance() + deltaDistance;
        }
        log.info("Initializing  with distance " + targetDistance + " inches");
    }

    @Override
    public void execute() {
        double power = travelManager.calculate(targetDistance, getYDistance());
        
        double leftPower = power - calculateHeadingPower();
        double rightPower = power + calculateHeadingPower();
        
        driveSubsystem.tankDrivePowerMode(leftPower, rightPower);
    }
    
    public double calculateHeadingPower() {

        double errorInDegrees = targetHeading.difference(poseSubsystem.getCurrentHeading());
        double normalizedError = errorInDegrees / 180;
        double rotationalPower = headingDrivePid.calculate(0, normalizedError);

        return rotationalPower;
    }
    
    private double getYDistance() {
        return poseSubsystem.getRobotOrientedTotalDistanceTraveled().y;
    }
    
    @Override
    public boolean isFinished() {
        double velocity = getYDistance() - previousPositionInches;
        previousPositionInches = getYDistance();
        boolean isOnTarget = travelManager.isOnTarget(distanceToleranceInches.get());
        boolean shouldFinish = velocity < 0.0001 && isOnTarget;
        if (shouldFinish) {
            onTargetCount++;
        } else {
            onTargetCount = 0;
        }
        return onTargetCount >= onTargetCountThresholdProp.get();
    }
    
    @Override
    public void end() {
        log.info("Ending, PreviousPosition was " + previousPositionInches 
                + ", Targeted Delta in distance is " + deltaDistance + " Distance traveled is " 
                + getYDistance());
        this.driveSubsystem.tankDrivePowerMode(0, 0);
    }
}
