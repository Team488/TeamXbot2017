package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
    
public class RunBeltIfWheelAtSpeedCommand extends BaseShooterBeltCommand {
    
    protected final ShooterWheelSubsystem shooterWheelSubsystem;
    private double targetBeltSpeed;

    public RunBeltIfWheelAtSpeedCommand(ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterBeltSubsystem);
        this.shooterWheelSubsystem = shooterWheelSubsystem;
        targetBeltSpeed = shooterBeltSubsystem.getTargetSpeed();
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (shooterWheelSubsystem.isAtSpeed()) {
            shooterBeltSubsystem.setTargetSpeed(targetBeltSpeed);
        } else {
            shooterBeltSubsystem.setPower(0);
        }
    }  
}
