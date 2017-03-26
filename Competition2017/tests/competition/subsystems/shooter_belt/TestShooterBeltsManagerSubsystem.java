package competition.subsystems.shooter_belt;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.DeferredTelemetryLogger;
import competition.subsystems.RobotSide;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDFactory;
import xbot.common.properties.XPropertyManager;

public class TestShooterBeltsManagerSubsystem extends ShooterBeltsManagerSubsystem {

    @Inject
    public TestShooterBeltsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory, Provider<DeferredTelemetryLogger> telemetryLogger){
        super(factory, propManager, pidFactory, telemetryLogger);
    }
    
    @Override
    protected void createLeftAndRightBelts(WPIFactory factory, XPropertyManager propManager, Provider<DeferredTelemetryLogger> telemetryLogger) {        
        leftBelt = new TestShooterBeltSubsystem(
                RobotSide.Left,
                leftMotorIndex,
                invertLeft,
                invertLeftSensor,
                factory, 
                leftPIDValues,
                propManager,
                telemetryLogger.get());
        
        rightBelt = new TestShooterBeltSubsystem(
                RobotSide.Right,
                rightMotorIndex,
                invertRight,
                invertRightSensor,
                factory, 
                rightPIDValues,
                propManager,
                telemetryLogger.get());
    }
}