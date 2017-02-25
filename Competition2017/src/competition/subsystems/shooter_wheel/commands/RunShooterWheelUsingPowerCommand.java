package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class RunShooterWheelUsingPowerCommand extends BaseShooterWheelCommand {

    public RunShooterWheelUsingPowerCommand(ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterWheelSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooterWheelSubsystem.setPower(.35);
    }

}
