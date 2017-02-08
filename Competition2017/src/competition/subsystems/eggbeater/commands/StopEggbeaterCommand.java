package competition.subsystems.eggbeater.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.eggbeater.EggbeaterSubsystem;
import xbot.common.command.BaseCommand;

public class StopEggbeaterCommand extends BaseCommand{
    final EggbeaterSubsystem eggbeaterSubsystem;
    private static Logger log = Logger.getLogger(StopEggbeaterCommand.class);
    
    @Inject
    public StopEggbeaterCommand(EggbeaterSubsystem eggbeaterSubsystem){
        this.eggbeaterSubsystem = eggbeaterSubsystem;   
        this.requires(eggbeaterSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing StopEggbeaterCommand");
    }

    @Override
    public void execute() {
        eggbeaterSubsystem.stop();        
    }
}
