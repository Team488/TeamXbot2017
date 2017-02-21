package competition.subsystems.shooter_wheel;

import competition.InfluxDBWriter;
import competition.subsystems.RobotSide;

import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.XPropertyManager;

public class TestShooterWheelSubsystem extends ShooterWheelSubsystem {

    public TestShooterWheelSubsystem(int masterChannel, int followerChannel, boolean masterInverted,
            boolean masterSensorInverted, boolean followerInverted, RobotSide side,
            PIDPropertyManager pidPropertyManager, WPIFactory factory, XPropertyManager propManager, InfluxDBWriter influxWriter) {
        super(masterChannel, followerChannel, masterInverted, masterSensorInverted, followerInverted, side, pidPropertyManager,
                factory, propManager, influxWriter);
    }

    public MockCANTalon getMasterMotor() {
        return (MockCANTalon)masterMotor;
    }
    
    public MockCANTalon getFollowerMotor() {
        return (MockCANTalon)followerMotor;
    }
    
}
