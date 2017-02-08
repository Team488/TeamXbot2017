package competition.subsystems.shooter_belt;

import competition.subsystems.RobotSide;
import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TestShooterBeltSubsystem extends ShooterBeltSubsystem {
    
    public TestShooterBeltSubsystem(int motor, RobotSide side, WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager) {
        super(motor, side, factory, propManager, assertionManager);
    }

    public DoubleProperty getIntakeProp(){
        return intakePowerProperty;
    }
    
    public DoubleProperty getEjectProp(){
        return ejectPowerProperty;
    }
    
    public MockCANTalon getMotor() {
        return (MockCANTalon)beltMotor;
    }
}
