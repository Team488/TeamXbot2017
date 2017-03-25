package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.command.BaseCommand;

public class FireTracerMode extends BaseCommand {
    protected final ShooterBeltSubsystem shooterBeltSubsystem;
    
    public FireTracerMode(ShooterBeltSubsystem shooterBeltSubsystem) {
        this.shooterBeltSubsystem = shooterBeltSubsystem; 
        this.requires(shooterBeltSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if(shooterBeltSubsystem.isAtSpeed()){
            shooterBeltSubsystem.intakeUsingSpeed();
        } else {
            shooterBeltSubsystem.setPower(0);
        }
    }  
}
