package competition.subsystems.climbing.commands;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;

public class StopClimbingCommand extends BaseClimbingCommand {
    
    @Inject
    public StopClimbingCommand(ClimbingSubsystem climbingSubsystem) {
        super(climbingSubsystem);
    }

    public void initialize() {
        log.info("Initializing");
    }

    public void execute(){
        climbingSubsystem.stop();
    }
}