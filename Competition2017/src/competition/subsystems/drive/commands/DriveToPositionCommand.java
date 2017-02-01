package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import xbot.common.command.BaseCommand;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.subsystems.drive.DriveSubsystem;

public class DriveToPositionCommand extends BaseCommand {

    private final DriveSubsystem driveSubsystem;
    private final PIDManager travelManager;
    
    private final DoubleProperty onTargetCountThresholdProp;
    private final DoubleProperty distanceToleranceInches;
    private double targetPosition;

    private double previousPositionInches;
    
    private int onTargetCount = 0;
    
    @Inject
    public DriveToPositionCommand(DriveSubsystem driveSubsystem, XPropertyManager propManager) {
        this.driveSubsystem = driveSubsystem;
        this.requires(driveSubsystem);
        this.travelManager = new PIDManager("Drive to position", propManager, 0.1, 0, 0.1, 0.5, -0.5);
        
        onTargetCountThresholdProp = propManager.createPersistentProperty("DrvToPos min stabilization time", 3);
        distanceToleranceInches = propManager.createPersistentProperty("Distance tolerance inches", 1.0);
    }
    
    /**
     * Sets the target position.
     * @param targetPosition the target in inches
     */
    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }
    
    @Override
    public void initialize() {
        previousPositionInches = driveSubsystem.getDisplacement();
        onTargetCount = 0;
    }

    @Override
    public void execute() {
        double power = travelManager.calculate(targetPosition, driveSubsystem.getDisplacement());
        
        driveSubsystem.tankDrivePowerMode(power, power);
    }
    
    @Override
    public boolean isFinished() {
        double velocity = driveSubsystem.getDisplacement() - previousPositionInches;
        previousPositionInches = driveSubsystem.getDisplacement();
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
