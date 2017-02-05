package competition.subsystems.feeder.commands;

import competition.subsystems.feeder.SideFeederSubsystem;
import xbot.common.command.BaseCommand;

public class ContinueFeedingCommand extends BaseCommand {

    final SideFeederSubsystem sideFeeder;

    public ContinueFeedingCommand(SideFeederSubsystem sideFeeder) {

        this.sideFeeder = sideFeeder;
        this.requires(this.sideFeeder);
    }
    
    @Override
    public void initialize() {

    }
    
    @Override
    public void execute() {

        sideFeeder.updateTelemetry();
    }
}