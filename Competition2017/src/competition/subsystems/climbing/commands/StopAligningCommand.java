package competition.subsystems.climbing.commands;

import com.google.inject.Inject;

import competition.subsystems.climbing.RopeAlignerSubsystem;
import xbot.common.command.BaseCommand;

public class StopAligningCommand extends BaseCommand {

    RopeAlignerSubsystem alignerSubsystem;
    
    @Inject
    public StopAligningCommand(RopeAlignerSubsystem alignerSubsystem) {
        this.alignerSubsystem = alignerSubsystem;
        this.requires(alignerSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        alignerSubsystem.stopIntake();
    }

}
