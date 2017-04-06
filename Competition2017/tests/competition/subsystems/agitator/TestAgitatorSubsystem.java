package competition.subsystems.agitator;

import competition.subsystems.RobotSide;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TestAgitatorSubsystem extends AgitatorSubsystem {

    public TestAgitatorSubsystem(
            int motor,
            RobotSide side,
            boolean invertMotor,
            WPIFactory factory,
            XPropertyManager propManager,
            RobotAssertionManager assertionManager) {
        super(motor, side, invertMotor, factory, propManager, assertionManager);
    }
    
    public double getOverCurrentThreshold() {
        return agitatorOverCurrentThreshold.get();
    }

    public double getStallDuration() {
        return agitatorStallDuration.get();
    }

    public double getUnjamDuration() {
        return unjamDuration.get();
    }
    
    public double getUnjamPower() {
        return unjamPower.get();
    }
}
