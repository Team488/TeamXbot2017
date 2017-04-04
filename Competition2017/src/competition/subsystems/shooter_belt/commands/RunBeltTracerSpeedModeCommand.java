package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class RunBeltTracerSpeedModeCommand extends BaseShooterBeltCommand {
    
    private ShooterWheelSubsystem shooterWheelSubsystem;
    
    public RunBeltTracerSpeedModeCommand(ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterBeltSubsystem);
        this.shooterWheelSubsystem = shooterWheelSubsystem;
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
