package competition.subsystems.collector.commands;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;

public class EjectCollectorCommand extends BaseCollectorCommand {
    
    @Inject
    public EjectCollectorCommand(CollectorSubsystem collectorSubsystem) {
        super(collectorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute(){
        collectorSubsystem.eject();
    }
}
