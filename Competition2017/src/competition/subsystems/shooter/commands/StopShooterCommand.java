package competition.subsystems.shooter.commands;

import competition.subsystems.shooter.SideShooterSubsystem;
import xbot.common.command.BaseCommand;

public class StopShooterCommand extends BaseCommand {
    
    final SideShooterSubsystem sideShooter;

    public StopShooterCommand(SideShooterSubsystem sideShooter) {
        this.sideShooter = sideShooter;
        this.requires(this.sideShooter);
    }

    @Override
    public void initialize() {

    }
    
    @Override
    public void execute() {
        sideShooter.setLauncherPower(0);
    }
    
}
