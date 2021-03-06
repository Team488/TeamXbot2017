package competition.subsystems.shooter_wheel;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.RobotSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ShooterWheelsManagerSubsystem extends BaseSubsystem {
    
    protected ShooterWheelSubsystem leftShooter;
    protected ShooterWheelSubsystem rightShooter;
    
    protected final int leftMotorMasterIndex = 32;
    protected final int leftMotorFollowerIndex = 33;
    protected final int rightMotorMasterIndex = 23;
    protected final int rightMotorFollowerIndex = 22;
    
    protected final PIDPropertyManager leftPIDValues;
    protected final PIDPropertyManager rightPIDValues;
    
    protected final BooleanProperty leftMasterInverted;
    protected final BooleanProperty leftMasterEncoderInverted;
    protected final BooleanProperty leftFollowerInverted;
    
    protected final BooleanProperty rightMasterInverted;
    protected final BooleanProperty rightMasterEncoderInverted;
    protected final BooleanProperty rightFollowerInverted;
    
    @Inject
    public ShooterWheelsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory) {
        log.info("Creating");
        
        leftPIDValues = pidFactory.createPIDPropertyManager(
                "LeftShooter", .5, 0, 10, .099);
        rightPIDValues = pidFactory.createPIDPropertyManager(
                "RightShooter", .5, 0, 10, .099);

        leftMasterInverted = propManager.createPersistentProperty("Left shooter master inverted", true);
        leftMasterEncoderInverted = propManager.createPersistentProperty("Left shooter master encoder inverted", false);
        leftFollowerInverted = propManager.createPersistentProperty("Left shooter follower inverted", false);

        rightMasterInverted = propManager.createPersistentProperty("Right shooter master inverted", false);
        rightMasterEncoderInverted = propManager.createPersistentProperty("Right shooter master encoder inverted", false);
        rightFollowerInverted = propManager.createPersistentProperty("Right shooter follower inverted", false);

        createLeftAndRightShooter(factory, propManager, pidFactory);
    }
    
    protected void createLeftAndRightShooter(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory) {
        leftShooter = new ShooterWheelSubsystem(
                leftMotorMasterIndex,
                leftMotorFollowerIndex,
                leftMasterInverted.get(),
                leftMasterEncoderInverted.get(),
                leftFollowerInverted.get(),
                RobotSide.Left,
                leftPIDValues,
                factory,
                propManager);
        
        rightShooter = new ShooterWheelSubsystem(
                rightMotorMasterIndex,
                rightMotorFollowerIndex,
                rightMasterInverted.get(),
                rightMasterEncoderInverted.get(),
                rightFollowerInverted.get(),
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
    
    public ShooterWheelSubsystem getShooterWheelForRobotSide(RobotSide side) {
        if (side == RobotSide.Right) {
            return rightShooter;
        }
        return leftShooter;
    }
}