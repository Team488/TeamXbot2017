package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;

public class RunShooterWheelsForRangeCommand extends BaseShooterWheelCommand {
    
    final double range;
    
    public RunShooterWheelsForRangeCommand(TypicalShootingPosition range, ShooterWheelSubsystem shooterWheelSubsystem) {
        super("RunShooterWheelsForRangeCommand: " + range, shooterWheelSubsystem);
        this.range = shooterWheelSubsystem.translateTypicalShootingPositionToDistance(range);
    }
    
    public RunShooterWheelsForRangeCommand(double rangeInInches, ShooterWheelSubsystem shooterWheelSubsystem) {
        super("RunShooterWheelsForRangeCommand: " + rangeInInches, shooterWheelSubsystem);
        this.range = rangeInInches;
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
