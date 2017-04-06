package competition.subsystems.agitator;

import com.google.inject.Inject;

import competition.subsystems.RobotSide;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.XPropertyManager;

public class TestAgitatorManagerSubsystem extends AgitatorsManagerSubsystem {

    @Inject
    public TestAgitatorManagerSubsystem(WPIFactory factory, XPropertyManager propManager,
            RobotAssertionManager assertionManager) {
        super(factory, propManager, assertionManager);
    }

    @Override
    protected void createLeftAndRightAgitators(WPIFactory factory, XPropertyManager propManager,
            RobotAssertionManager assertionManager) {
        leftAgitator = new TestAgitatorSubsystem(leftMotorIndex, RobotSide.Left, true, factory, propManager,
                assertionManager);
        rightAgitator = new TestAgitatorSubsystem(rightMotorIndex, RobotSide.Right, false, factory, propManager,
                assertionManager);
    }

}