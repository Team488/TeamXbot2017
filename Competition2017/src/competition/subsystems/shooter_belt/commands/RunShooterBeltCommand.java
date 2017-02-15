package competition.subsystems.shooter_belt.commands;

import org.apache.log4j.Logger;
import competition.subsystems.shooter_belt.ShooterBeltSubsystem;

public class RunShooterBeltCommand extends BaseShooterBeltCommand {
    
    private static Logger log = Logger.getLogger(RunShooterBeltCommand.class);
    
    double targetBeltSpeed;
    
    public RunShooterBeltCommand(ShooterBeltSubsystem shooterBeltSubsystem) {
        super(shooterBeltSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing " +  this.getName());
    }

    @Override
    public void execute() {
        shooterBeltSubsystem.setTargetSpeed(targetBeltSpeed);
    }
}