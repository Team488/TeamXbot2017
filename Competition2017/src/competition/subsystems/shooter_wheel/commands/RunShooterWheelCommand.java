package competition.subsystems.shooter_wheel.commands;

import org.apache.log4j.Logger;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class RunShooterWheelCommand extends BaseShooterWheelCommand {

    private static Logger log = Logger.getLogger(ContinueShooterCommand.class);
    
    protected final DoubleProperty commandedLauncherSpeed;

    public RunShooterWheelCommand(ShooterWheelSubsystem shooterWheelSubsystem, XPropertyManager propertyManager) {
        super(shooterWheelSubsystem);
        commandedLauncherSpeed = propertyManager.createPersistentProperty("Commanded launcher speed", 3100.0);
    }
    
    @Override
    public void initialize(){
        log.info("Initializing RunShooterCommand");
    }
    
    @Override 
    public void execute(){
        shooterWheelSubsystem.setTargetSpeed(commandedLauncherSpeed.get());
    }
    
    
    
    
    
    
    
    
    
}
