package competition.subsystems.shooter_wheel;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.RobotSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ShooterWheelsManagerSubsystem extends BaseSubsystem {
    
    private static Logger log = Logger.getLogger(ShooterWheelsManagerSubsystem.class);
    private ShooterWheelSubsystem leftShooter;
    private ShooterWheelSubsystem rightShooter;
    
    @Inject
    public ShooterWheelsManagerSubsystem(WPIFactory factory, XPropertyManager propManager){
        log.info("Creating ShooterSubsystem");
         this.leftShooter = new ShooterWheelSubsystem(20, RobotSide.Left, factory, propManager);
         this.rightShooter = new ShooterWheelSubsystem(30, RobotSide.Right, factory, propManager);
    }
    public ShooterWheelSubsystem getLeftShooter(){
        return leftShooter;
    }
    public ShooterWheelSubsystem getRightShooter(){
        return rightShooter;
    }
   
    
}
