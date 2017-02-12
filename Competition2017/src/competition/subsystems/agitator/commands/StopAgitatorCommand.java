package competition.subsystems.agitator.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorSubsystem;
import xbot.common.command.BaseCommand;

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
