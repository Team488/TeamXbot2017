package competition.subsystems.feeder;

import competition.subsystems.RobotSide;
import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TestSideFeederSubsystem extends SideFeederSubsystem {
    
    public TestSideFeederSubsystem(int motor, RobotSide side, WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager) {
        super(motor, side, factory, propManager, assertionManager);
    }

    public DoubleProperty getIntakeProp(){
        return intakePowerProperty;
    }
    
    public DoubleProperty getEjectProp(){
        return ejectPowerProperty;
    }
    
    public MockCANTalon getMotor() {
        return (MockCANTalon)feederMotor;
    }
}
