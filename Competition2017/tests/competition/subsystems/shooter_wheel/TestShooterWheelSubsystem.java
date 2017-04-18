package competition.subsystems.shooter_wheel;

import competition.subsystems.RobotSide;
import edu.wpi.first.wpilibj.MockServo;
import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.XPropertyManager;

public class TestShooterWheelSubsystem extends ShooterWheelSubsystem {

    public TestShooterWheelSubsystem(int masterChannel, int followerChannel, int servoChannel, boolean masterInverted,
            boolean masterSensorInverted, boolean followerInverted, boolean servoInverted, RobotSide side,
            PIDPropertyManager pidPropertyManager, WPIFactory factory, XPropertyManager propManager) {
        super(masterChannel, followerChannel, servoChannel, masterInverted, masterSensorInverted, followerInverted, servoInverted, side, pidPropertyManager,
                factory, propManager);
    }

    public MockCANTalon getMasterMotor() {
        return (MockCANTalon)masterMotor;
    }

    public MockCANTalon getFollowerMotor() {
        return (MockCANTalon)followerMotor;
    }

    public MockServo getAimServo() {
        return (MockServo)aimServo;
    }
    
}
