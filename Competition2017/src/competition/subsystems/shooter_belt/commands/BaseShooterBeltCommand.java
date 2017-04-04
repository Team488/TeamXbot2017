package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseShooterBeltCommand extends BaseCommand {
    
    protected final ShooterBeltSubsystem shooterBeltSubsystem;
    
    public BaseShooterBeltCommand(ShooterBeltSubsystem shooterBeltSubsystem) {
        this.shooterBeltSubsystem = shooterBeltSubsystem;
        this.requires(shooterBeltSubsystem);
    }
}
