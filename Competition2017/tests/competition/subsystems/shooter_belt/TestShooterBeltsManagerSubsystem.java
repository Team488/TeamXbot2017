package competition.subsystems.shooter_belt;

import com.google.inject.Inject;

import competition.subsystems.RobotSide;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.XPropertyManager;

public class TestShooterBeltsManagerSubsystem extends ShooterBeltsManagerSubsystem {

    @Inject
    public TestShooterBeltsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager){
        super(factory, propManager, assertionManager);
    }
    
    @Override
    protected void createLeftAndRightBelts(WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager) {
        leftBelt = new TestShooterBeltSubsystem(leftMotorIndex, RobotSide.Left, factory, propManager, assertionManager);
        rightBelt = new TestShooterBeltSubsystem(rightMotorIndex, RobotSide.Right, factory, propManager, assertionManager);
    }
}