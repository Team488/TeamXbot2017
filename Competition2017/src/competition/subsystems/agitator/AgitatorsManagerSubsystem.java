package competition.subsystems.agitator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import competition.subsystems.RobotSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.XPropertyManager;

@Singleton
public class AgitatorsManagerSubsystem extends BaseSubsystem {
 
        protected AgitatorSubsystem leftAgitator;
        protected AgitatorSubsystem rightAgitator;
        protected int leftMotorIndex = 29;
        protected int rightMotorIndex = 26;

        @Inject
        public AgitatorsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager){
            log.info("Creating");
            createLeftAndRightAgitators(factory, propManager, assertionManager);
        }
        
        protected void createLeftAndRightAgitators(WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager) {
            leftAgitator = new AgitatorSubsystem(leftMotorIndex, RobotSide.Left, true, factory, propManager, assertionManager);
            rightAgitator = new AgitatorSubsystem(rightMotorIndex, RobotSide.Right, false, factory, propManager, assertionManager);
        }

        public AgitatorSubsystem getLeftAgitator(){
            return leftAgitator;
        }

        public AgitatorSubsystem getRightAgitator(){
            return rightAgitator;
        }
}