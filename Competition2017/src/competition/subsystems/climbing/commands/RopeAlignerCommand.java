package competition.subsystems.climbing.commands;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;
import competition.subsystems.climbing.RopeAlignerSubsystem;
import xbot.common.command.BaseCommand;

public class RopeAlignerCommand extends BaseCommand {
    
    private final RopeAlignerSubsystem ropeAlignerSubsystem;
    
    @Inject
    public RopeAlignerCommand(RopeAlignerSubsystem ropeAlignerSubsystem) {
        this.ropeAlignerSubsystem = ropeAlignerSubsystem;
        this.requires(ropeAlignerSubsystem);
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
