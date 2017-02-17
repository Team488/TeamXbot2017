package competition.subsystems.climbing.commands;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;

public class DescendClimbingCommand extends BaseClimbingCommand {
    
    @Inject
    public DescendClimbingCommand(ClimbingSubsystem climbingSubsystem) {
        super(climbingSubsystem);
    }

    public void initialize() {
        log.info("Initializing");
    }

    public void execute(){
        climbingSubsystem.descend();
    }
}