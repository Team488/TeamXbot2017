package competition.subsystems.shooter.commands;

import competition.subsystems.shooter.SideShooterSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class SetShooterSpeedCommand extends BaseCommand {
    
    final SideShooterSubsystem sideShooter;
    final DoubleProperty commandedLauncherSpeed;

    public SetShooterSpeedCommand(SideShooterSubsystem sideShooter, XPropertyManager propertyManager) {
        this.sideShooter = sideShooter;
        this.requires(this.sideShooter);
        
        commandedLauncherSpeed = propertyManager.createEphemeralProperty("Commanded launcher speed", 3100.0);
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
