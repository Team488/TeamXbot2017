package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.RobotSide;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class UnjamShooterWheelCommand extends BaseShooterWheelCommand {

    private DoubleProperty threshold;

    public UnjamShooterWheelCommand(RobotSide side, ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem, XPropertyManager propMan) {
        super(shooterWheelsManagerSubsystem.getShooterForRobotSide(side));
        
        threshold = propMan.createPersistentProperty("threshold for jammed motor speed", 0.01);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (shooterWheelSubsystem.getMotorSpeed() >= 0 && shooterWheelSubsystem.getMotorSpeed() <= threshold.get()
                && shooterWheelSubsystem.getMotorPower() > 0) {
            shooterWheelSubsystem.setPower(shooterWheelSubsystem.getMotorPower() * -1);
        }
    }

}
