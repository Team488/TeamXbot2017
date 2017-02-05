package competition.subsystems.feeder.commands;

import competition.subsystems.feeder.SideFeederSubsystem;

import xbot.common.command.BaseCommand;

public class StopFeederCommand extends BaseCommand {

    final SideFeederSubsystem sideFeeder;

    public StopFeederCommand(SideFeederSubsystem sideFeeder) {

        this.sideFeeder = sideFeeder;
        this.requires(this.sideFeeder);
    }
    
    @Override
    public void initialize() {
        
        sideFeeder.setFeederPower(0);
    }

    @Override
    public void execute() {
        
    }
}
