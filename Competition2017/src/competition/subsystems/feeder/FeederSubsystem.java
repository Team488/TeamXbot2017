package competition.subsystems.feeder;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.RobotSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.XPropertyManager;

@Singleton
public class FeederSubsystem extends BaseSubsystem {

        private static Logger log = Logger.getLogger(FeederSubsystem.class);
        
        protected SideFeederSubsystem leftFeeder;
        protected SideFeederSubsystem rightFeeder;
        
        protected int leftMotorIndex = 31;
        protected int rightMotorIndex = 44;

        @Inject
        public FeederSubsystem(WPIFactory factory, XPropertyManager propManager){
            log.info("Creating FeederSubsystem");
            createLeftAndRightFeeders(factory, propManager);
        }
        
        protected void createLeftAndRightFeeders(WPIFactory factory, XPropertyManager propManager) {
            leftFeeder = new SideFeederSubsystem(leftMotorIndex, RobotSide.Left, factory, propManager);
            rightFeeder = new SideFeederSubsystem(rightMotorIndex, RobotSide.Right, factory, propManager);
        }

        public SideFeederSubsystem getLeftFeeder(){

            return leftFeeder;
        }

        public SideFeederSubsystem getRightFeeder(){
            
            return rightFeeder;
        }
}