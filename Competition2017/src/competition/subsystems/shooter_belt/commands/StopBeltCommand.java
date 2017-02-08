package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.command.BaseCommand;

public class StopBeltCommand extends BaseCommand {

    final ShooterBeltSubsystem beltSubsystem;

    public StopBeltCommand(ShooterBeltSubsystem beltSubsystem) {

        this.beltSubsystem = beltSubsystem;
        this.requires(this.beltSubsystem);
    }
    
    @Override
    public void initialize() {
        
        beltSubsystem.setBeltPower(0);
    }

    @Override
    public void execute() {
        
    }
}
