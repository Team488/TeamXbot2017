package competition.subsystems.eggbeater.commands;

import org.apache.log4j.Logger;
import com.google.inject.Inject;

import competition.subsystems.eggbeater.EggbeaterSubsystem;
import xbot.common.command.BaseCommand;

public class SpinEggbeaterForwardsCommand extends BaseCommand{
    final EggbeaterSubsystem eggbeaterSubsystem;
    private static Logger log = Logger.getLogger(SpinEggbeaterForwardsCommand.class);
    
    @Inject
    public SpinEggbeaterForwardsCommand(EggbeaterSubsystem eggbeaterSubsystem){
        this.eggbeaterSubsystem = eggbeaterSubsystem;   
        this.requires(eggbeaterSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing SpinEggbeaterForwardsCommand");
    }

    @Override
    public void execute() {
        eggbeaterSubsystem.spinForwards();        
    }

}
