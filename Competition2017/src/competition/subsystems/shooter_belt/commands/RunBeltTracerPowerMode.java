package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class RunBeltTracerPowerMode extends BaseShooterBeltCommand {

    private ShooterWheelSubsystem shooterWheelSubsystem;
    
    public RunBeltTracerPowerMode(ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterBeltSubsystem);
        this.shooterWheelSubsystem = shooterWheelSubsystem;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (shooterWheelSubsystem.isWheelAtSpeed()) {
            shooterBeltSubsystem.intakeUsingTracerPower();
        } else {
            shooterBeltSubsystem.setPower(0);
        }
    }  
}
