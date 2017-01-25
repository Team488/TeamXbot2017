package competition.subsystems.collector.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;
import xbot.common.command.BaseCommand;


public class StopCollectorCommand extends BaseCommand {
    final CollectorSubsystem collectorSystem;
    private static Logger log = Logger.getLogger(StopCollectorCommand.class);
    
    @Inject
    public StopCollectorCommand(CollectorSubsystem collectorSystem){
        this.collectorSystem = collectorSystem;   
        this.requires(collectorSystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing StopCollectorCommand");
        
    }
    
    @Override
    public void execute(){
        collectorSystem.stop();
    }

}