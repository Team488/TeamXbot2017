package competition.subsystems.shooter_wheel;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.RobotSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDPropertyManager;
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
    
    PIDPropertyManager leftPIDValues;
    PIDPropertyManager rightPIDValues;
    
    boolean leftMasterInverted = false;
    boolean leftMasterEncoderInverted = false;
    boolean leftFollowerInverted = true;
    
    boolean rightMasterInverted = true;
    boolean rightMasterEncoderInverted = true;
    boolean rightFollowerInverted = true; 
    
    @Inject
    public ShooterWheelsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory) {
        log.info("Creating ShooterSubsystem");
        
        leftPIDValues = pidFactory.createPIDPropertyManager(
                "LeftShooter", .5, 0, 10, .099);
        rightPIDValues = pidFactory.createPIDPropertyManager(
                "LeftShooter", .5, 0, 10, .099);
        
        createLeftAndRightShooter(factory, propManager, pidFactory);
    }
    
    protected void createLeftAndRightShooter(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory) {
        leftShooter = new ShooterWheelSubsystem(
                leftMotorMasterIndex,
                leftMotorFollowerIndex,
                leftMasterInverted,
                leftMasterEncoderInverted,
                leftFollowerInverted,
                RobotSide.Left,
                leftPIDValues,
                factory,
                propManager);
        
        rightShooter = new ShooterWheelSubsystem(
                rightMotorMasterIndex,
                rightMotorFollowerIndex,
                rightMasterInverted,
                rightMasterEncoderInverted,
                rightFollowerInverted,
                RobotSide.Right,
                rightPIDValues,
                factory,
                propManager);
    }
    
    public ShooterWheelSubsystem getLeftShooter() {
        return leftShooter;
    }
    public ShooterWheelSubsystem getRightShooter() {
        return rightShooter;
    }
}