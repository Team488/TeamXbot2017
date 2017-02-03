package competition.subsystems.climbing.commands;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import competition.subsystems.climbing.ClimbingSubsystem;
import xbot.common.command.BaseCommand;

public class DescendClimbingCommand extends BaseCommand {

    private static Logger log = Logger.getLogger(DescendClimbingCommand.class);

    final ClimbingSubsystem climbingSystem;

    @Inject
    public DescendClimbingCommand(ClimbingSubsystem climbingSystem){

        this.climbingSystem = climbingSystem; 
        this.requires(climbingSystem);
    }

    public void initialize() {

        log.info("Initializing DescendClimbingComand");
    }

    public void execute(){

        climbingSystem.descend();
    }
}