package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TrimShooterWheelCommand extends BaseCommand {

    public enum TrimDirection {
        Up,
        Down
    }
    
    final DoubleProperty trimAmount;
    final ShooterWheelSubsystem shooterWheelSubsystem;
    private TrimDirection direction;
    
    public TrimShooterWheelCommand(ShooterWheelSubsystem shooterWheelSubsystem, XPropertyManager propMan) {
        this.shooterWheelSubsystem = shooterWheelSubsystem;
        
        trimAmount = propMan.createPersistentProperty("Trim shooter wheel amount", 100);
        direction = TrimDirection.Up;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        
        double amount = trimAmount.get() * ((direction == TrimDirection.Up) ? 1 : -1); 
        
        shooterWheelSubsystem.trimRangePower(TypicalShootingPosition.FlushToBoiler, amount);
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
