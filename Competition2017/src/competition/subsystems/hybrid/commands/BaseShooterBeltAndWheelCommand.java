package competition.subsystems.hybrid.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseShooterBeltAndWheelCommand extends BaseCommand {
    
    protected final ShooterBeltSubsystem shooterBeltSubsystem;
    protected final ShooterWheelSubsystem shooterWheelSubsystem;
    
    public BaseShooterBeltAndWheelCommand(ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        this.shooterBeltSubsystem = shooterBeltSubsystem;
        this.shooterWheelSubsystem = shooterWheelSubsystem;
        this.requires(shooterBeltSubsystem);
    }
}
