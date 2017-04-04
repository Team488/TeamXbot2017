package competition.subsystems.shooter_belt.commands;

import competition.subsystems.hybrid.commands.BaseShooterBeltAndWheelCommand;
import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class RunBeltSlowlyUsingPowerCommand extends BaseShooterBeltAndWheelCommand {

    public RunBeltSlowlyUsingPowerCommand(ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterBeltSubsystem, shooterWheelSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (shooterWheelSubsystem.isWheelAtSpeed()) {
            shooterBeltSubsystem.intakeUsingTracerSpeed();
        } else {
            shooterBeltSubsystem.setPower(0);
        }
    }  
}
