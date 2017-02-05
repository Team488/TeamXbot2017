package competition.subsystems.climbing.commands;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import competition.subsystems.climbing.ClimbingSubsystem;
import xbot.common.command.BaseCommand;

public class AscendCommand extends BaseCommand {
    
    final ClimbingSubsystem climbingSystem;
    
    private static Logger log = Logger.getLogger(AscendCommand.class);   

    @Inject
    public AscendCommand(ClimbingSubsystem climbingSystem){

        this.climbingSystem = climbingSystem;  
        this.requires(climbingSystem);
    }

    public void initialize() {

        log.info("Initializing AscendCommand");
    }

    public void execute(){

        climbingSystem.ascend();
    }
}