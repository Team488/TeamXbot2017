package competition.subsystems.shooter_wheel.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class RunShooterCommand extends BaseCommand {
    
    private static Logger log = Logger.getLogger(RunShooterCommand.class);
    
    ShooterWheelSubsystem shooterWheel;
    DoubleProperty commandedLauncherSpeed;
    
    @Inject
    public RunShooterCommand(XPropertyManager propertyManager){
        commandedLauncherSpeed = propertyManager.createEphemeralProperty("Commanded launcher speed", 3100.0);
    }
    
    public void setSide(ShooterWheelSubsystem shooterWheel){
        this.shooterWheel = shooterWheel;
        this.requires(shooterWheel);
    }
    
    @Override
    public void initialize(){
        log.info("Initializing RunShooterCommand");
    }
    
    @Override 
    public void execute(){
        shooterWheel.setTargetSpeed(commandedLauncherSpeed.get());
        shooterWheel.updatePeriodicData();
    }
    
    
    
    
    
    
    
    
    
}
