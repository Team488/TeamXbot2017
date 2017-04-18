package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;

public class StopBeltCommand extends BaseShooterBeltCommand {
    
    public StopBeltCommand(ShooterBeltSubsystem shooterBeltSubsystem) {
        super(shooterBeltSubsystem);
        this.setRunWhenDisabled(true);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        shooterBeltSubsystem.stop();
    }
}
