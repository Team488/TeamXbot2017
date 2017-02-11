package competition.subsystems.shooter_belt.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.command.BaseCommand;

public class StopBeltCommand extends BaseCommand {

    ShooterBeltSubsystem beltSubsystem;

    @Inject
    public StopBeltCommand() {
    }
    
    public void setShooterBeltSubsystem(ShooterBeltSubsystem beltSubsystem) {
        this.beltSubsystem = beltSubsystem;
        this.requires(this.beltSubsystem);
    }
    
    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        beltSubsystem.setBeltPower(0);
    }
}
