package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveInfinitelyCommand extends BaseDriveCommand{
    private DoubleProperty power;

    @Inject
    public DriveInfinitelyCommand(DriveSubsystem driveSubsystem, XPropertyManager propMan) {
        super(driveSubsystem);
        this.requires(driveSubsystem);
        power = propMan.createPersistentProperty("power of inifinite driving", 1);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        driveSubsystem.tankDrivePowerMode(power.get(), power.get());
    }

}
