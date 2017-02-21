package competition.subsystems.shooter_belt;

import com.google.inject.Inject;

import competition.subsystems.RobotSide;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import telemetry.InfluxDBConnection;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDFactory;
import xbot.common.properties.XPropertyManager;

public class TestShooterBeltsManagerSubsystem extends ShooterBeltsManagerSubsystem {

    @Inject
    public TestShooterBeltsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory, InfluxDBConnection influxConnection){
        super(factory, propManager, pidFactory, influxConnection);
    }
    
    @Override
    protected void createLeftAndRightBelts(WPIFactory factory, XPropertyManager propManager, InfluxDBConnection influxConnection) {        
        leftBelt = new TestShooterBeltSubsystem(
                RobotSide.Left,
                leftMotorIndex,
                invertLeft,
                invertLeftSensor,
                factory, 
                leftPIDValues,
                propManager,
                influxConnection);
        
        rightBelt = new TestShooterBeltSubsystem(
                RobotSide.Right,
                rightMotorIndex,
                invertRight,
                invertRightSensor,
                factory, 
                rightPIDValues,
                propManager,
                influxConnection);
    }
}