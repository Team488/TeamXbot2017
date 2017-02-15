package competition.subsystems.climbing.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;

public class StopClimbingCommand extends BaseClimbingCommand {

    private static Logger log = Logger.getLogger(StopClimbingCommand.class);
    
    @Inject
    public StopClimbingCommand(ClimbingSubsystem climbingSubsystem) {
        super(climbingSubsystem);
    }

    public void initialize() {
        log.info("Initializing StopClimbingCommand");
    }

    public void execute(){
        climbingSubsystem.stop();
    }
}