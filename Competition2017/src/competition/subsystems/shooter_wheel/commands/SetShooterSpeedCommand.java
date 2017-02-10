package competition.subsystems.shooter_wheel.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class SetShooterSpeedCommand extends BaseCommand {
    
    ShooterWheelSubsystem shooterWheel;
    DoubleProperty commandedLauncherSpeed;

    @Inject
    public SetShooterSpeedCommand(XPropertyManager propertyManager) {
        commandedLauncherSpeed = propertyManager.createEphemeralProperty("Commanded launcher speed", 3100.0);
    }
    
    public void setSide(ShooterWheelSubsystem shooterWheel){
        this.shooterWheel = shooterWheel;
        this.requires(this.shooterWheel);
    }

    @Override
    public void initialize() {
    }
    
    @Override
    public void execute() {
        shooterWheel.setLauncherTargetSpeed(commandedLauncherSpeed.get());
        shooterWheel.updateTelemetry();
    }
    
}
