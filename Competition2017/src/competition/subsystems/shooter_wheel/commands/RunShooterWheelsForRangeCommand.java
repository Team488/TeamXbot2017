package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;

public class RunShooterWheelsForRangeCommand extends BaseShooterWheelCommand {
    
    protected TypicalShootingPosition range;
    protected final boolean isDelayed;
    
    public RunShooterWheelsForRangeCommand(TypicalShootingPosition range, ShooterWheelSubsystem shooterWheelSubsystem, double delay) {
        super("RunShooterWheelsForRangeCommand", shooterWheelSubsystem);
        this.range = range;
        
        if(isDelayed = (delay > 0)) {
            setTimeout(delay);
        }
    }
    
    public RunShooterWheelsForRangeCommand(TypicalShootingPosition range, ShooterWheelSubsystem shooterWheelSubsystem) {
        this(range, shooterWheelSubsystem, 0);
    }
    
    public void setTargetRange(TypicalShootingPosition range) {
        this.range = range;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        if(!isDelayed || isTimedOut()) {
            shooterWheelSubsystem.runForRange(range);
        }
    }

    @Override
    public void execute() {
        if(!isDelayed || isTimedOut()) {
            shooterWheelSubsystem.runForRange(range);
        }
    }
}
