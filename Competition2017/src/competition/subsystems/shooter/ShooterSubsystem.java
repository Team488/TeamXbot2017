package competition.subsystems.shooter;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.RobotSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ShooterSubsystem extends BaseSubsystem {
    
    private static Logger log = Logger.getLogger(ShooterSubsystem.class);
    private SideShooterSubsystem leftShooter;
    private SideShooterSubsystem rightShooter;
    
    @Inject
    public ShooterSubsystem(WPIFactory factory, XPropertyManager propManager){
        log.info("Creating ShooterSubsystem");
         this.leftShooter = new SideShooterSubsystem(2, RobotSide.Left, factory, propManager);
         this.rightShooter = new SideShooterSubsystem(3, RobotSide.Right, factory, propManager);
    }
    public SideShooterSubsystem getLeftShooter(){
        return leftShooter;
    }
    public SideShooterSubsystem getRightShooter(){
        return rightShooter;
    }
   
    
}
