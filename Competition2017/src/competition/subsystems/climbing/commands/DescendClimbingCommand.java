package competition.subsystems.climbing.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;

public class DescendClimbingCommand extends BaseClimbingCommand {

    private static Logger log = Logger.getLogger(DescendClimbingCommand.class);
    
    @Inject
    public DescendClimbingCommand(ClimbingSubsystem climbingSubsystem) {
        super(climbingSubsystem);
    }

    public void initialize() {
        log.info("Initializing DescendClimbingComand");
    }

    public void execute(){
        climbingSubsystem.descend();
    }
}