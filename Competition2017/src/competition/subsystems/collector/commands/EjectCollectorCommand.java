package competition.subsystems.collector.commands;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import competition.subsystems.collector.CollectorSubsystem;

public class EjectCollectorCommand extends BaseCollectorCommand {
    
    private static Logger log = Logger.getLogger(EjectCollectorCommand.class);
    
    @Inject
    public EjectCollectorCommand(CollectorSubsystem collectorSubsystem) {
        super(collectorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing EjectCollectorComand");
    }
    
    @Override
    public void execute(){
        collectorSubsystem.eject();
    }
}
