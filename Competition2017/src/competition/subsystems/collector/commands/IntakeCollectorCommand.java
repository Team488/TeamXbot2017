package competition.subsystems.collector.commands;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;

public class IntakeCollectorCommand extends BaseCollectorCommand {
    
    @Inject
    public IntakeCollectorCommand(CollectorSubsystem collectorSubsystem) {
        super(collectorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute(){
        collectorSubsystem.intake();
    }
}
