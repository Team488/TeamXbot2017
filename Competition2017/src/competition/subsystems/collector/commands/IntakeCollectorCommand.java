package competition.subsystems.collector.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;

public class IntakeCollectorCommand extends BaseCollectorCommand {
    
    private static Logger log = Logger.getLogger(IntakeCollectorCommand.class);
    
    @Inject
    public IntakeCollectorCommand(CollectorSubsystem collectorSubsystem) {
        super(collectorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing IntakeCollectorCommand");
    }
    
    @Override
    public void execute(){
        collectorSubsystem.intake();
    }
}
