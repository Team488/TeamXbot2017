package competition.subsystems.shooter_belt;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.RobotSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ShooterBeltsManagerSubsystem extends BaseSubsystem {

        private static Logger log = Logger.getLogger(ShooterBeltsManagerSubsystem.class);
        
        protected ShooterBeltSubsystem leftBelt;
        protected ShooterBeltSubsystem rightBelt;
        
        protected PIDPropertyManager leftPIDValues;
        protected PIDPropertyManager rightPIDValues;
        
        boolean invertLeft = false;
        boolean invertLeftSensor = false;
        
        boolean invertRight = true;
        boolean invertRightSensor = true;
        
        protected int leftMotorIndex = 31;
        protected int rightMotorIndex = 24;

        @Inject
        public ShooterBeltsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory){
            log.info("Creating ShooterBeltSubsystem");
            
            leftPIDValues = pidFactory.createPIDPropertyManager("LeftBelt", 0, 0, 0, 0);
            rightPIDValues = pidFactory.createPIDPropertyManager("RightBelt", 0, 0, 0, 0);
            
            createLeftAndRightBelts(factory, propManager);
        }
        
        protected void createLeftAndRightBelts(WPIFactory factory, XPropertyManager propManager) {
            leftBelt = new ShooterBeltSubsystem(
                    RobotSide.Left,
                    leftMotorIndex,
                    invertLeft,
                    invertLeftSensor,
                    factory, 
                    leftPIDValues,
                    propManager);
            
            rightBelt = new ShooterBeltSubsystem(
                    RobotSide.Right,
                    rightMotorIndex,
                    invertRight,
                    invertRightSensor,
                    factory, 
                    rightPIDValues,
                    propManager);
        }

        public ShooterBeltSubsystem getLeftBelt(){
            return leftBelt;
        }

        public ShooterBeltSubsystem getRightBelt(){
            return rightBelt;
        }
}