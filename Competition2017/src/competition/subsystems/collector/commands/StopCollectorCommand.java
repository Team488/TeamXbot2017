package competition.subsystems.collector.commands;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;

public class StopCollectorCommand extends BaseCollectorCommand {
    
    @Inject
    public StopCollectorCommand(CollectorSubsystem collectorSubsystem) {
        super(collectorSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");  
    }
    
    @Override
    public void execute(){
        collectorSubsystem.stop();
    }

}