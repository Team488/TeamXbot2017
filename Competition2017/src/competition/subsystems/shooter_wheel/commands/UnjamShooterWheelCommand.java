package competition.subsystems.shooter_wheel.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class UnjamShooterWheelCommand extends BaseShooterWheelCommand {

    private DoubleProperty threshold;

    @Inject
    public UnjamShooterWheelCommand(ShooterWheelSubsystem shooterWheelSubsystem, XPropertyManager propMan) {
        super(shooterWheelSubsystem);

        threshold = propMan.createPersistentProperty("threshold for jammed motor speed", 0.01);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        if (shooterWheelSubsystem.getMotorSpeed() >= 0 && shooterWheelSubsystem.getMotorSpeed() <= threshold.get()
                && shooterWheelSubsystem.getMotorPower() > 0) {
            shooterWheelSubsystem.setPower(shooterWheelSubsystem.getMotorPower() * -1);
        }
    }

}
