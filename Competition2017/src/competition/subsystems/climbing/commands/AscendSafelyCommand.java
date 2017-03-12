package competition.subsystems.climbing.commands;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;

public class AscendSafelyCommand extends BaseClimbingCommand {

    @Inject
    public AscendSafelyCommand(ClimbingSubsystem climbingSubsystem) {
        super(climbingSubsystem);
    }

    public void initialize() {
        log.info("Initializing");
        climbingSubsystem.setEnableSafeties(true);
    }

    public void execute() {
        climbingSubsystem.ascend();
    }
}