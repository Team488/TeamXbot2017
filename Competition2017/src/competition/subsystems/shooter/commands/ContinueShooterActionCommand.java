package competition.subsystems.shooter.commands;

import competition.subsystems.shooter.SideShooterSubsystem;
import xbot.common.command.BaseCommand;

public class ContinueShooterActionCommand extends BaseCommand {

    final SideShooterSubsystem sideShooter;

    public ContinueShooterActionCommand(SideShooterSubsystem sideShooter) {
        this.sideShooter = sideShooter;
        this.requires(this.sideShooter);
    }

    @Override
    public void initialize() {
    
    }
    
    @Override
    public void execute() {
        sideShooter.updateTelemetry();
    }
    
}
