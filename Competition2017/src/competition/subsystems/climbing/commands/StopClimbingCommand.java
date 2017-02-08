package competition.subsystems.climbing.commands;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import competition.subsystems.climbing.ClimbingSubsystem;
import xbot.common.command.BaseCommand;

public class StopClimbingCommand extends BaseCommand {

    final ClimbingSubsystem climbingSystem;
   
    private static Logger log = Logger.getLogger(StopClimbingCommand.class);

    @Inject
    public StopClimbingCommand(ClimbingSubsystem climbingSystem){

        this.climbingSystem = climbingSystem;   
        this.requires(climbingSystem);
    }

    public void initialize() {
        log.info("Initializing StopClimbingCommand");
    }

    public void execute(){
        climbingSystem.stop();
    }
}