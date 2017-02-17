package competition.subsystems.shooter_belt;

import competition.subsystems.RobotSide;
import competition.subsystems.shooter_belt.ShooterBeltSubsystem;

import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TestShooterBeltSubsystem extends ShooterBeltSubsystem {
    
    public TestShooterBeltSubsystem(
            RobotSide side,
            int masterChannel,
            boolean invertMaster,
            boolean invertMasterSensor,
            WPIFactory factory, 
            PIDPropertyManager pidPropertyManager,
            XPropertyManager propManager){
        super(
                side,
                masterChannel,
                invertMaster,
                invertMasterSensor,
                factory,
                pidPropertyManager,
                propManager);
    }

    public DoubleProperty getIntakeProp(){
        return intakePowerProperty;
    }
    
    public DoubleProperty getEjectProp(){
        return ejectPowerProperty;
    }
    
    public MockCANTalon getMotor() {
        return (MockCANTalon)masterMotor;
    }
}
