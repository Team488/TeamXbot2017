package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class ContinueShooterCommand extends BaseShooterWheelCommand {  

    public ContinueShooterCommand(ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterWheelSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initialzing");
    }

    @Override
    public void execute() {
        shooterWheelSubsystem.updatePeriodicData();
    }
}
