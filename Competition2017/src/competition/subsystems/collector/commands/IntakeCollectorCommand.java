package competition.subsystems.collector.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;
import xbot.common.command.BaseCommand;


public class IntakeCollectorCommand extends BaseCommand {
    final CollectorSubsystem collectorSystem;
    private static Logger log = Logger.getLogger(IntakeCollectorCommand.class);
    
    @Inject
    public IntakeCollectorCommand(CollectorSubsystem collectorSystem){
        this.collectorSystem = collectorSystem;  
        this.requires(collectorSystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing IntakeCollectorCommand");
    }
    
    @Override
    public void execute(){
        collectorSystem.intake();
    }
}
