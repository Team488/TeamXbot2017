package competition.subsystems.climbing.commands;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;

public class AscendCommand extends BaseClimbingCommand {

    @Inject
    public AscendCommand(ClimbingSubsystem climbingSubsystem) {
        super(climbingSubsystem);
    }

    public void initialize() {
        log.info("Initializing");
    }

    public void execute() {
        climbingSubsystem.ascend();
    }
}