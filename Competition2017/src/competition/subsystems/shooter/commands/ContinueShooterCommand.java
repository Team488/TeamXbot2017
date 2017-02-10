package competition.subsystems.shooter.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter.ShooterSubsystem;
import competition.subsystems.shooter.SideShooterSubsystem;
import xbot.common.command.BaseCommand;

public class ContinueShooterCommand extends BaseCommand {

    SideShooterSubsystem sideShooter;

    @Inject
    public ContinueShooterCommand() {
    }
    
    public void setSide(SideShooterSubsystem sideshooter) {
        this.sideShooter = sideshooter;
        this.requires(sideshooter);
    }

    @Override
    public void initialize() {
    
    }
    
    @Override
    public void execute() {
        sideShooter.updateTelemetry();
    }
    
}
