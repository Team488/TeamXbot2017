package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseShooterWheelCommand extends BaseCommand {
    
    protected final ShooterWheelSubsystem shooterWheelSubsystem;
    
    public BaseShooterWheelCommand(ShooterWheelSubsystem shooterWheelSubsystem) {
        this.shooterWheelSubsystem = shooterWheelSubsystem; 
        this.requires(shooterWheelSubsystem);
    }
    
    public BaseShooterWheelCommand(String name, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(name);
        this.shooterWheelSubsystem = shooterWheelSubsystem; 
        this.requires(shooterWheelSubsystem);
    }
}
