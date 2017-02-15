package competition.subsystems.climbing.commands;

import competition.subsystems.climbing.ClimbingSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseClimbingCommand extends BaseCommand {
    
    final ClimbingSubsystem climbingSubsystem;
    
    public BaseClimbingCommand(ClimbingSubsystem climbingSubsystem) {
        this.climbingSubsystem = climbingSubsystem; 
        this.requires(climbingSubsystem);
    }
}
