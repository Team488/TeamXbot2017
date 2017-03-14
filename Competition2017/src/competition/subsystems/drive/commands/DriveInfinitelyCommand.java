package competition.subsystems.drive.commands;

import java.util.function.DoubleSupplier;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveInfinitelyCommand extends BaseDriveCommand {
    private double power = 0;
    private DoubleSupplier powerTargetSupplier;

    @Inject
    public DriveInfinitelyCommand(DriveSubsystem driveSubsystem, XPropertyManager propMan) {
        super(driveSubsystem);
        this.requires(driveSubsystem);
    }
    
    public void setDrivePower(double power) {
        powerTargetSupplier = () -> power;
    }
    
    public void setDrivePowerProp(DoubleProperty prop) {
        powerTargetSupplier = () -> prop.get();
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        power = powerTargetSupplier == null ? 0 : powerTargetSupplier.getAsDouble();
    }

    @Override
    public void execute() {
        driveSubsystem.tankDrivePowerMode(power, power);
    }
}
