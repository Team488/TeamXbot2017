package competition.subsystems.climb;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TestClimbingSubsystem extends ClimbingSubsystem {

    @Inject
    public TestClimbingSubsystem(WPIFactory factory, XPropertyManager propManager) {

        super(factory, propManager);
    }

    public DoubleProperty getAscendProp(){

        return ascendPowerProperty;
    }
    
    public XCANTalon getMotor(){
        return climbingMotor;
    }

    public DoubleProperty getDescendProp(){

        return descendPowerProperty;
    }
}