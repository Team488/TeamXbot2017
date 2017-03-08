package competition.subsystems.shooter_wheel;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.DeferredTelemetryLogger;
import competition.subsystems.RobotSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDPropertyManager;
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
    
    protected final boolean leftMasterInverted = true;
    protected final boolean leftMasterEncoderInverted = false;
    protected final boolean leftFollowerInverted = false;
    
    protected final boolean rightMasterInverted = true;
    protected final boolean rightMasterEncoderInverted = true;
    protected final boolean rightFollowerInverted = false;
    
    @Inject
    public ShooterWheelsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory, DeferredTelemetryLogger telemetryLogger) {
        log.info("Creating");
        
        leftPIDValues = pidFactory.createPIDPropertyManager(
                "LeftShooter", .5, 0, 10, .099);
        rightPIDValues = pidFactory.createPIDPropertyManager(
                "LeftShooter", .5, 0, 10, .099);
                
        createLeftAndRightShooter(factory, propManager, pidFactory, telemetryLogger);
    }
    
    protected void createLeftAndRightShooter(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory, DeferredTelemetryLogger telemetryLogger) {
        leftShooter = new ShooterWheelSubsystem(
                leftMotorMasterIndex,
                leftMotorFollowerIndex,
                leftMasterInverted,
                leftMasterEncoderInverted,
                leftFollowerInverted,
                RobotSide.Left,
                leftPIDValues,
                factory,
                propManager,
                telemetryLogger);
        
        rightShooter = new ShooterWheelSubsystem(
                rightMotorMasterIndex,
                rightMotorFollowerIndex,
                rightMasterInverted,
                rightMasterEncoderInverted,
                rightFollowerInverted,
                RobotSide.Right,
                rightPIDValues,
                factory,
                propManager,
                telemetryLogger);
    }
    
    public ShooterWheelSubsystem getLeftShooter() {
        return leftShooter;
    }
    public ShooterWheelSubsystem getRightShooter() {
        return rightShooter;
    }
}