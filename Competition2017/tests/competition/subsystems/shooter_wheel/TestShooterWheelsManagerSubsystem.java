package competition.subsystems.shooter_wheel;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.RobotSide;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDFactory;
import xbot.common.properties.XPropertyManager;

@Singleton
public class TestShooterWheelsManagerSubsystem extends ShooterWheelsManagerSubsystem {

    @Inject
    public TestShooterWheelsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory) {
        super(factory, propManager, pidFactory);
    }
    
    @Override
    protected void createLeftAndRightShooter(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory) {        
        leftShooter = new TestShooterWheelSubsystem(
                leftMotorMasterIndex,
                leftMotorFollowerIndex,
                leftAimServoIndex,
                leftMasterInverted.get(),
                leftMasterEncoderInverted.get(),
                leftFollowerInverted.get(),
                leftServoInverted.get(),
                RobotSide.Left,
                leftPIDValues,
                factory,
                propManager);
        
        rightShooter = new TestShooterWheelSubsystem(
                rightMotorMasterIndex,
                rightMotorFollowerIndex,
                rightAimServoIndex,
                rightMasterInverted.get(),
                rightMasterEncoderInverted.get(),
                rightFollowerInverted.get(),
                rightServoInverted.get(),
                RobotSide.Right,
                rightPIDValues,
                factory,
                propManager);
    }
}
