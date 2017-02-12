package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseAgitatorCommand extends BaseCommand {
    final AgitatorSubsystem agitatorSubsystem;
    
    public BaseAgitatorCommand(AgitatorSubsystem agitatorSubsystem) {
        this.agitatorSubsystem = agitatorSubsystem; 
        this.requires(agitatorSubsystem);
    }
}
