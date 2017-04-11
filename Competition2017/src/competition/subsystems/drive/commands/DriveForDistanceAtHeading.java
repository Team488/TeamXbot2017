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

public class DriveForDistanceAtHeading extends BaseDriveCommand {
    protected final DoubleProperty speedHeadingThresh;
    protected final PoseSubsystem poseSubsystem;
    protected ContiguousHeading targetHeading;
    protected double targetDistance;

    protected DoubleSupplier targetHeadingSupplier;
    protected DoubleSupplier targetDistanceSupplier;
    
    private final PIDManager headingDrivePid;
    private final PIDManager distancePid;
    private final PIDManager alignmentPid;
    
    private XYPair initialPosition = null;
    
    @Inject
    public DriveForDistanceAtHeading(
            DriveSubsystem driveSubsystem, 
            XPropertyManager propMan,
            PoseSubsystem pose,
            RobotAssertionManager assertionManager,
            PIDFactory pidFactory)
    {
        super(driveSubsystem);
        this.poseSubsystem = pose;
        speedHeadingThresh = propMan.createPersistentProperty("Drive at heading threshold for translation", 5);
        
        headingDrivePid = new PIDManager("Drive at heading rotation", propMan, assertionManager,  1 / 80d, 0, 0);
        
        headingDrivePid.setErrorThreshold(3); // error in degrees, since degrees are used for the calculate call in execute()
        headingDrivePid.setDerivativeThreshold(3.0 / 20); // 3 degrees per second, divided by 20, since derivative is calculated 20 times a second
        
        headingDrivePid.setEnableErrorThreshold(true);
        headingDrivePid.setEnableDerivativeThreshold(true);
        
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

        targetHeading = new ContiguousHeading(targetHeadingSupplier.getAsDouble());
        targetDistance = targetDistanceSupplier.getAsDouble();
        
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
                    .add(initialPosition.scale(-1))
                    .rotate(-targetHeading.getValue());
            
            double translationPowerFactor = Math.max(speedHeadingThresh.get() - Math.abs(errorInDegrees), 0) / speedHeadingThresh.get();
            double translationPower = distancePid.calculate(targetDistance, targetRelativePositionalError.y) * translationPowerFactor;
            
            // Using the translation power as a multiplication factor ensures that we only align when we are moving toward the goal
            double alignmentPower = alignmentPid.calculate(targetRelativePositionalError.x, 0) * Math.abs(translationPower);
            
            driveSubsystem.tankDrivePowerMode(translationPower + rotationalPower - alignmentPower, translationPower - rotationalPower + alignmentPower);
        }
    }
    
    @Override
    public boolean isFinished() {
        return targetHeading != null && headingDrivePid.isOnTarget() && distancePid.isOnTarget();
    }
    
    @Override 
    public void end(){
        log.info("Ending. Current heading is " + poseSubsystem.getCurrentHeading() + " and target heading was" 
                + targetHeading);
    }
    
}