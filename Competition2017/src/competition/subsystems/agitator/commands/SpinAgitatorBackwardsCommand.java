package competition.subsystems.agitator.commands;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import competition.subsystems.agitator.AgitatorSubsystem;
import xbot.common.command.BaseCommand;

public class SpinAgitatorBackwardsCommand extends BaseCommand{
    final AgitatorSubsystem agitatorSubsystem;
    private static Logger log = Logger.getLogger(SpinAgitatorBackwardsCommand.class);
    
    @Inject
    public SpinAgitatorBackwardsCommand(AgitatorSubsystem agitatorSubsystem){
        this.agitatorSubsystem = agitatorSubsystem;   
        this.requires(agitatorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing SpinAgitatorBackwardsCommand");
    }

    @Override
    public void execute() {
        agitatorSubsystem.spinBackwards();        
    }
}
