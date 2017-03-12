package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TrimShooterWheelCommand extends BaseCommand {

    final DoubleProperty trimAmount;
    final ShooterWheelSubsystem shooterWheelSubsystem;
    private TrimDirection direction;
    
    public TrimShooterWheelCommand(ShooterWheelSubsystem shooterWheelSubsystem, XPropertyManager propMan) {
        this.shooterWheelSubsystem = shooterWheelSubsystem;
        
        trimAmount = propMan.createPersistentProperty("Trim shooter wheel amount", 200);
        direction = TrimDirection.Up;
    }
    
    public enum TrimDirection {
        Up,
        Down
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        shooterWheelSubsystem.trimRangePower(TypicalShootingPosition.FlushToBoiler, trimAmount.get());
    }
    
    public void setTrimDirection(TrimDirection direction) {
        this.direction = direction;
    }

    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
