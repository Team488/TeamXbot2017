package competition.subsystems.shooter_wheel.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class UnjamShooterWheelCommand extends BaseShooterWheelCommand {
    private ShooterWheelSubsystem subsystem;
    private DoubleProperty threshold;

    @Inject
    public UnjamShooterWheelCommand(ShooterWheelSubsystem shooterWheelSubsystem, XPropertyManager propMan) {
        super(shooterWheelSubsystem);
        subsystem = shooterWheelSubsystem;
        threshold = propMan.createPersistentProperty("threshold for jammed motor speed", 0.01);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        if (subsystem.getMotorSpeed() >= 0 && subsystem.getMotorSpeed() <= threshold.get()
                && subsystem.getMotorPower() > 0) {
            subsystem.setPower(subsystem.getMotorPower() * -1);
        }
    }

}
