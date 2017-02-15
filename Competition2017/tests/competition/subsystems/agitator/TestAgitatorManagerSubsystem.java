package competition.subsystems.agitator;

import com.google.inject.Inject;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.XPropertyManager;

public class TestAgitatorManagerSubsystem extends AgitatorsManagerSubsystem {
    
    @Inject
    public TestAgitatorManagerSubsystem(WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager) {
        super(factory, propManager, assertionManager);
    }

}