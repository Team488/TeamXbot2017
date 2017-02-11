package competition.subsystems.shooter_belt;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.RobotSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ShooterBeltsManagerSubsystem extends BaseSubsystem {

        private static Logger log = Logger.getLogger(ShooterBeltsManagerSubsystem.class);
        
        protected ShooterBeltSubsystem leftBelt;
        protected ShooterBeltSubsystem rightBelt;
        
        protected int leftMotorIndex = 32;
        protected int rightMotorIndex = 34;

        @Inject
        public ShooterBeltsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager){
            log.info("Creating ShooterBeltSubsystem");
            createLeftAndRightBelts(factory, propManager, assertionManager);
        }
        
        protected void createLeftAndRightBelts(WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager) {
            leftBelt = new ShooterBeltSubsystem(leftMotorIndex, RobotSide.Left, factory, propManager, assertionManager);
            rightBelt = new ShooterBeltSubsystem(rightMotorIndex, RobotSide.Right, factory, propManager, assertionManager);
        }

        public ShooterBeltSubsystem getLeftBelt(){
            return leftBelt;
        }

        public ShooterBeltSubsystem getRightBelt(){
            return rightBelt;
        }
}