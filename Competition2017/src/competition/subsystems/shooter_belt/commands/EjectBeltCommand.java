package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;

public class EjectBeltCommand extends BaseShooterBeltCommand {

    public EjectBeltCommand(ShooterBeltSubsystem shooterBeltSubsystem) {
        super(shooterBeltSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        
    }

    @Override
    public void execute() {
        this.shooterBeltSubsystem.eject();
    }

}
