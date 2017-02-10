package competition.subsystems.shooter.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter.SideShooterSubsystem;
import xbot.common.command.BaseCommand;

public class StopShooterCommand extends BaseCommand {
    
    SideShooterSubsystem sideShooter;
    
    @Inject
    public StopShooterCommand() {}

    public void setSide(SideShooterSubsystem sideShooter){
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
