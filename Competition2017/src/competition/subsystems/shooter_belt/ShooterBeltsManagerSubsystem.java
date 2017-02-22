package competition.subsystems.shooter_belt;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.RobotSide;
import telemetry.InfluxDBConnection;
import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ShooterBeltsManagerSubsystem extends BaseSubsystem {        

        protected ShooterBeltSubsystem leftBelt;
        protected ShooterBeltSubsystem rightBelt;
        
        protected final PIDPropertyManager leftPIDValues;
        protected final PIDPropertyManager rightPIDValues;
        
        protected final boolean invertLeft = true;
        protected final boolean invertLeftSensor = true;
        
        protected final boolean invertRight = false;
        protected final boolean invertRightSensor = false;
        
        protected final int leftMotorIndex = 31;
        protected final int rightMotorIndex = 24;

        @Inject
        public ShooterBeltsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory, InfluxDBConnection influxConnection){
            log.info("Creating");
            leftPIDValues = pidFactory.createPIDPropertyManager("LeftBelt", 0, 0, 0, 0);
            rightPIDValues = pidFactory.createPIDPropertyManager("RightBelt", 0, 0, 0, 0);
 
            createLeftAndRightBelts(factory, propManager, influxConnection);
        }
        
        protected void createLeftAndRightBelts(WPIFactory factory, XPropertyManager propManager, InfluxDBConnection influxConnection) {
            leftBelt = new ShooterBeltSubsystem(
                    RobotSide.Left,
                    leftMotorIndex,
                    invertLeft,
                    invertLeftSensor,
                    factory, 
                    leftPIDValues,
                    propManager,
                    influxConnection);
            
            rightBelt = new ShooterBeltSubsystem(
                    RobotSide.Right,
                    rightMotorIndex,
                    invertRight,
                    invertRightSensor,
                    factory, 
                    rightPIDValues,
                    propManager,
                    influxConnection);
        }

        public ShooterBeltSubsystem getLeftBelt(){
            return leftBelt;
        }

        public ShooterBeltSubsystem getRightBelt(){
            return rightBelt;
        }
}