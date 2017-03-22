package competition.subsystems.collector;

import com.google.inject.Inject;


import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TestCollectorSubsystem extends CollectorSubsystem {
    
    @Inject
    public TestCollectorSubsystem(WPIFactory factory, XPropertyManager propManager) {
        super(factory, propManager);
    }
 
    public DoubleProperty getLowIntakeProp() {
        return lowPowerCollector;
    }
    
    public DoubleProperty getEjectProp(){
        return ejectPowerProperty;
    }
}
