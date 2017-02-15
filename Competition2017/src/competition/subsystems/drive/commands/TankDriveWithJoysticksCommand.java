package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;

public class TankDriveWithJoysticksCommand extends BaseDriveCommand {
    
    final OperatorInterface oi;
    
    @Inject
    public TankDriveWithJoysticksCommand(OperatorInterface oi, DriveSubsystem driveSubsystem) {
        super(driveSubsystem);
        this.oi = oi;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        driveSubsystem.tankDrivePowerMode(oi.leftJoystick.getVector().y, oi.rightJoystick.getVector().y);
    }
}
