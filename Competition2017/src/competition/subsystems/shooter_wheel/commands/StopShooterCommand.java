package competition.subsystems.shooter_wheel.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;

public class StopShooterCommand extends BaseCommand {
    
    ShooterWheelSubsystem shooterWheel;
    
    @Inject
    public StopShooterCommand() {}

    public void setSide(ShooterWheelSubsystem shooterWheel){
        this.shooterWheel = shooterWheel;
        this.requires(this.shooterWheel);
    }
    @Override
    public void initialize() {

    }
    
    @Override
    public void execute() {
        shooterWheel.setLauncherPower(0);
    }
    
}
