package competition.subsystems.agitator.commands;

import org.apache.log4j.Logger;
import competition.subsystems.agitator.AgitatorSubsystem;

public class StopAgitatorCommand extends BaseAgitatorCommand {

    private static Logger log = Logger.getLogger(StopAgitatorCommand.class);
    
    public StopAgitatorCommand(AgitatorSubsystem agitatorSubsystem) {
        super(agitatorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing StopAgitatorCommand");
    }

    @Override
    public void execute() {
        agitatorSubsystem.stop();
    }
}
