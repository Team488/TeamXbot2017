package competition.subsystems.feeder;

import com.google.inject.Inject;

import competition.subsystems.RobotSide;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.XPropertyManager;

public class TestFeederSubsystem extends FeederSubsystem {

    @Inject
    public TestFeederSubsystem(WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager){
        super(factory, propManager, assertionManager);
    }
    
    @Override
    protected void createLeftAndRightFeeders(WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager) {
        leftFeeder = new TestSideFeederSubsystem(leftMotorIndex, RobotSide.Left, factory, propManager, assertionManager);
        rightFeeder = new TestSideFeederSubsystem(rightMotorIndex, RobotSide.Right, factory, propManager, assertionManager);
    }
}