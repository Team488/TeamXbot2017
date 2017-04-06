package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class StopShooterCommand extends BaseShooterWheelCommand {
    
    public StopShooterCommand(ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterWheelSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute() {
        shooterWheelSubsystem.setPower(0);
    }
}
