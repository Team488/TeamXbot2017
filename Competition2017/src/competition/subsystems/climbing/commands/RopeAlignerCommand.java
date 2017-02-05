package competition.subsystems.climbing.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.climbing.RopeAlignerSubsystem;
import xbot.common.command.BaseCommand;

public class RopeAlignerCommand extends BaseCommand {
    private static Logger log = Logger.getLogger(RopeAlignerCommand.class);
    
    private final RopeAlignerSubsystem ropeAlignerSubsystem;
    
    @Inject
    public RopeAlignerCommand(RopeAlignerSubsystem ropeAlignerSubsystem) {
        this.ropeAlignerSubsystem = ropeAlignerSubsystem;
        this.requires(ropeAlignerSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing RopeAlignerCommand");
    }

    @Override
    public void execute() {
        ropeAlignerSubsystem.intake();
    }
}
