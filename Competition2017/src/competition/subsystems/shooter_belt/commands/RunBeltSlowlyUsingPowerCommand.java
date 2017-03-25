package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class RunBeltSlowlyUsingPowerCommand extends BaseShooterBeltCommand {
    protected final ShooterWheelSubsystem shooterWheelSubsystem;

    public RunBeltSlowlyUsingPowerCommand(ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterBeltSubsystem);
        this.shooterWheelSubsystem = shooterWheelSubsystem;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        shooterBeltSubsystem.intakeUsingTracerPower();
    }  
}
