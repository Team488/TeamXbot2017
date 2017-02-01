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
    
    private final DoubleProperty distanceToleranceInches;
    private double targetPosition;
    
    @Inject
    public DriveToPositionCommand(DriveSubsystem driveSubsystem, XPropertyManager propManager) {
        this.driveSubsystem = driveSubsystem;
        this.requires(driveSubsystem);
        this.travelManager = new PIDManager("Drive to position", propManager, 0.1, 0, 0.1);
        
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

    }

    @Override
    public void execute() {
        double power = travelManager.calculate(targetPosition, driveSubsystem.getDisplacement());
 
        driveSubsystem.tankDrivePowerMode(power, power);
    }
    
    @Override
    public boolean isFinished() {
        return travelManager.isOnTarget(distanceToleranceInches.get());
    }
}
