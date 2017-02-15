package competition.subsystems.collector.commands;

import competition.subsystems.collector.CollectorSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseCollectorCommand extends BaseCommand {
    
    protected final CollectorSubsystem collectorSubsystem;
    
    public BaseCollectorCommand(CollectorSubsystem collectorSubsystem) {
        this.collectorSubsystem = collectorSubsystem; 
        this.requires(collectorSubsystem);
    }
}
