package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;

public class RunShooterBeltPowerCommand extends BaseShooterBeltCommand {

    public RunShooterBeltPowerCommand(ShooterBeltSubsystem shooterBeltSubsystem) {
        super(shooterBeltSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        shooterBeltSubsystem.intakeUsingPower();
    }
}
