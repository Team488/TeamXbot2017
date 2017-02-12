package competition.subsystems.agitator.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.commands.EjectAgitatorCommand;
import xbot.common.command.BaseCommand;

public class EjectAgitatorCommand extends BaseAgitatorCommand {
    
    private static Logger log = Logger.getLogger(EjectAgitatorCommand.class);
        
    public EjectAgitatorCommand(AgitatorSubsystem agitatorSystem) {
        super(agitatorSystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing EjectAgitatorComand");
    }
    
    @Override
    public void execute(){
        agitatorSubsystem.eject();
    }
}