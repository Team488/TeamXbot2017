package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class RunBeltTracerModeCommand extends BaseShooterBeltCommand {
    
    public RunBeltTracerModeCommand(ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterBeltSubsystem, shooterWheelSubsystem);
        this.requires(shooterBeltSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if(shooterWheelSubsystem.isAtSpeed()){
            shooterBeltSubsystem.intakeUsingTracerSpeed();
        } else {
            shooterBeltSubsystem.setPower(0);
        }
    }  
}
