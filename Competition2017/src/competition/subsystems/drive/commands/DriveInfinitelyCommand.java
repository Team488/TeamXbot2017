package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.properties.XPropertyManager;

public class DriveInfinitelyCommand extends BaseDriveCommand{
    private double power = 0;

    @Inject
    public DriveInfinitelyCommand(DriveSubsystem driveSubsystem, XPropertyManager propMan) {
        super(driveSubsystem);
        this.requires(driveSubsystem);
    }
    
    public void setDrivePower(double power) {
        this.power = power;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        driveSubsystem.tankDrivePowerMode(power, power);
    }
}
