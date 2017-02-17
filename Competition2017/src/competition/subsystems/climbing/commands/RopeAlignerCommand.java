package competition.subsystems.climbing.commands;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;
import competition.subsystems.climbing.RopeAlignerSubsystem;

public class RopeAlignerCommand extends BaseClimbingCommand {
    
    private final RopeAlignerSubsystem ropeAlignerSubsystem;
    
    @Inject
    public RopeAlignerCommand(RopeAlignerSubsystem ropeAlignerSubsystem, ClimbingSubsystem climbingSubsystem) {
        super(climbingSubsystem);
        this.ropeAlignerSubsystem = ropeAlignerSubsystem;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        ropeAlignerSubsystem.intake();
    }
}
