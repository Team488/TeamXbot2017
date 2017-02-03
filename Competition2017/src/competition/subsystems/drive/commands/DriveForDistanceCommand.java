package competition.subsystems.drive.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.command.BaseCommand;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.subsystems.drive.DriveSubsystem;

public class DriveForDistanceCommand extends BaseCommand {
    private static Logger log = Logger.getLogger(DriveForDistanceCommand.class);
    
    private final DriveSubsystem driveSubsystem;
    private final PIDManager travelManager;
    
    private final DoubleProperty onTargetCountThresholdProp;
    private final DoubleProperty distanceToleranceInches;
    
    private DoubleProperty deltaDistanceProp;
    private double deltaDistance;
    
    private double targetDistance;

    private double previousPositionInches;
    
    private int onTargetCount = 0;
    
    @Inject
    public DriveForDistanceCommand(DriveSubsystem driveSubsystem, XPropertyManager propManager) {
        this.driveSubsystem = driveSubsystem;
        this.requires(driveSubsystem);
        this.travelManager = new PIDManager("Drive to position", propManager, 0.1, 0, 0, 0.5, -0.5);
        
        onTargetCountThresholdProp = propManager.createPersistentProperty("DrvToPos min stabilization time", 3);
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
        previousPositionInches = driveSubsystem.getDistance();
        
        if (deltaDistanceProp != null) {
            this.targetDistance = driveSubsystem.getDistance() + deltaDistanceProp.get();
        } else {
            this.targetDistance = driveSubsystem.getDistance() + deltaDistance;
        }
        log.info("Initializing DriveForDistanceCommand with distance " + targetDistance + " inches");
    }

    @Override
    public void execute() {
        double power = travelManager.calculate(targetDistance, driveSubsystem.getDistance());
        
        driveSubsystem.tankDrivePowerMode(power, power);
    }
    
    @Override
    public boolean isFinished() {
        double velocity = driveSubsystem.getDistance() - previousPositionInches;
        previousPositionInches = driveSubsystem.getDistance();
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
        this.driveSubsystem.tankDrivePowerMode(0, 0);
    }
}
