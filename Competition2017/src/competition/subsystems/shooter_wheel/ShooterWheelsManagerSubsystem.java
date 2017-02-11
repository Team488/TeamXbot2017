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
    
    protected ShooterWheelSubsystem leftShooter;
    protected ShooterWheelSubsystem rightShooter;
    
    protected int leftMotorMasterIndex = 32;
    protected int leftMotorFollowerIndex = 33;
    protected int rightMotorMasterIndex = 23;
    protected int rightMotorFollowerIndex = 22;
    
    @Inject
    public ShooterWheelsManagerSubsystem(WPIFactory factory, XPropertyManager propManager) {
        log.info("Creating ShooterSubsystem");
        createLeftAndRightShooter(factory, propManager);
    }
    
    protected void createLeftAndRightShooter(WPIFactory factory, XPropertyManager propManager) {
        leftShooter = new ShooterWheelSubsystem(leftMotorMasterIndex, leftMotorFollowerIndex, RobotSide.Left, factory, propManager);
        rightShooter = new ShooterWheelSubsystem(rightMotorMasterIndex, rightMotorFollowerIndex, RobotSide.Right, factory, propManager);
    }
    
    public ShooterWheelSubsystem getLeftShooter() {
        return leftShooter;
    }
    public ShooterWheelSubsystem getRightShooter() {
        return rightShooter;
    }
}