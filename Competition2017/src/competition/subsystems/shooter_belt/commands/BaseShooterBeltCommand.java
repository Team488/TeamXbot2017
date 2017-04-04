package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseShooterBeltCommand extends BaseCommand {
    
    protected final ShooterBeltSubsystem shooterBeltSubsystem;
    protected final ShooterWheelSubsystem shooterWheelSubsystem;
    
    public BaseShooterBeltCommand(ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        this.shooterBeltSubsystem = shooterBeltSubsystem;
        this.shooterWheelSubsystem = shooterWheelSubsystem;
        this.requires(shooterBeltSubsystem);
    }
    
    public BaseShooterBeltCommand(ShooterBeltSubsystem shooterBeltSubsystem) {
        this.shooterBeltSubsystem = shooterBeltSubsystem;
        this.shooterWheelSubsystem = null;
        this.requires(shooterBeltSubsystem);
    }
}
