package competition.subsystems.shooter.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter.SideShooterSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class SetShooterSpeedCommand extends BaseCommand {
    
    SideShooterSubsystem sideShooter;
    DoubleProperty commandedLauncherSpeed;

    @Inject
    public SetShooterSpeedCommand(XPropertyManager propertyManager) {
        commandedLauncherSpeed = propertyManager.createEphemeralProperty("Commanded launcher speed", 3100.0);
    }
    
    public void setSide(SideShooterSubsystem sideShooter){
        this.sideShooter = sideShooter;
        this.requires(this.sideShooter);
    }

    @Override
    public void initialize() {
    }
    
    @Override
    public void execute() {
        sideShooter.setLauncherTargetSpeed(commandedLauncherSpeed.get());
        sideShooter.updateTelemetry();
    }
    
}
