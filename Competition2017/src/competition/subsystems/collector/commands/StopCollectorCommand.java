package competition.subsystems.collector.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;

public class StopCollectorCommand extends BaseCollectorCommand {

    private static Logger log = Logger.getLogger(StopCollectorCommand.class);
    
    @Inject
    public StopCollectorCommand(CollectorSubsystem collectorSubsystem) {
        super(collectorSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing StopCollectorCommand");
        
    }
    
    @Override
    public void execute(){
        collectorSubsystem.stop();
    }

}