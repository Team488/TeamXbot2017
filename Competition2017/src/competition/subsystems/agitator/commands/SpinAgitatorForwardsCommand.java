package competition.subsystems.agitator.commands;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import competition.subsystems.agitator.AgitatorSubsystem;
import xbot.common.command.BaseCommand;

public class SpinAgitatorForwardsCommand extends BaseCommand{
    final AgitatorSubsystem agitatorSubsystem;
    private static Logger log = Logger.getLogger(SpinAgitatorForwardsCommand.class);
    
    @Inject
    public SpinAgitatorForwardsCommand(AgitatorSubsystem agitatorSubsystem){
        this.agitatorSubsystem = agitatorSubsystem;   
        this.requires(agitatorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing SpinAgitatorForwardsCommand");
    }

    @Override
    public void execute() {
        agitatorSubsystem.spinForwards();        
    }

}
