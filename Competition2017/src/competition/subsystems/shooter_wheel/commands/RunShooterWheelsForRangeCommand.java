package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;

public class RunShooterWheelsForRangeCommand extends BaseShooterWheelCommand {
    
    protected TypicalShootingPosition range;
    
    public RunShooterWheelsForRangeCommand(TypicalShootingPosition range, ShooterWheelSubsystem shooterWheelSubsystem) {
        super("RunShooterWheelsForRangeCommand", shooterWheelSubsystem);
        this.range = range;
    }
    
    public void setTargetRange(TypicalShootingPosition range) {
        this.range = range;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        shooterWheelSubsystem.runForRange(range);
    }

    @Override
    public void execute() {
        shooterWheelSubsystem.runForRange(range);
    }
}
