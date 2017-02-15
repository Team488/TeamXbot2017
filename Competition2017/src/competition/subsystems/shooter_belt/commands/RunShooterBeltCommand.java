package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;

public class RunShooterBeltCommand extends BaseShooterBeltCommand {
    
    double targetBeltSpeed;
    
    public RunShooterBeltCommand(ShooterBeltSubsystem shooterBeltSubsystem) {
        super(shooterBeltSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        shooterBeltSubsystem.setTargetSpeed(targetBeltSpeed);
    }
}