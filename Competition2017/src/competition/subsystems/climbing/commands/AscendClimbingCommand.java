package competition.subsystems.climbing.commands;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;

public class AscendClimbingCommand extends BaseClimbingCommand {

    @Inject
    public AscendClimbingCommand(ClimbingSubsystem climbingSubsystem) {
        super(climbingSubsystem);
    }

    public void initialize() {
        log.info("Initializing");
    }

    public void execute() {
        climbingSubsystem.ascend();
    }
}