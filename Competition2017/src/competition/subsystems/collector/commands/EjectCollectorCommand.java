package competition.subsystems.collector.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;
import xbot.common.command.BaseCommand;


public class EjectCollectorCommand extends BaseCommand {
    private static Logger log = Logger.getLogger(EjectCollectorCommand.class);
    final CollectorSubsystem collectorSystem;
    
    @Inject
    public EjectCollectorCommand(CollectorSubsystem collectorSystem){
        this.collectorSystem = collectorSystem; 
        this.requires(collectorSystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing EjectCollectorComand");
    }
    
    @Override
    public void execute(){
        collectorSystem.eject();
    }

}
