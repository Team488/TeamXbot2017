package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;

public class RunShooterWheelsForRangeCommand extends BaseShooterWheelCommand {
    
    protected TypicalShootingPosition range;
    
    public RunShooterWheelsForRangeCommand(TypicalShootingPosition range, ShooterWheelSubsystem shooterWheelSubsystem) {
        super("RunShooterWheelsForRangeCommand: " + range, shooterWheelSubsystem);
        this.range = range;
    }
    
    public void setTargetRange(TypicalShootingPosition range) {
        this.range = range;
        // TODO: Update name
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        shooterWheelSubsystem.setTargetSpeedForRange(range);
    }
}
