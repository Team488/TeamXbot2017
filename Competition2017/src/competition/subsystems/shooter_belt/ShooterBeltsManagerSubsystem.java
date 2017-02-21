package competition.subsystems.shooter_belt;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.InfluxDBWriter;
import competition.subsystems.RobotSide;

import xbot.common.command.BaseSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ShooterBeltsManagerSubsystem extends BaseSubsystem {        

        protected ShooterBeltSubsystem leftBelt;
        protected ShooterBeltSubsystem rightBelt;
        
        protected final PIDPropertyManager leftPIDValues;
        protected final PIDPropertyManager rightPIDValues;
        
        protected final boolean invertLeft = false;
        protected final boolean invertLeftSensor = false;
        
        protected final boolean invertRight = true;
        protected final boolean invertRightSensor = true;
        
        protected final int leftMotorIndex = 31;
        protected final int rightMotorIndex = 24;
       
        @Inject
        public ShooterBeltsManagerSubsystem(WPIFactory factory, XPropertyManager propManager, PIDFactory pidFactory, InfluxDBWriter influxWriter){
            log.info("Creating");
            leftPIDValues = pidFactory.createPIDPropertyManager("LeftBelt", 0, 0, 0, 0);
            rightPIDValues = pidFactory.createPIDPropertyManager("RightBelt", 0, 0, 0, 0);
            
            createLeftAndRightBelts(factory, propManager, influxWriter);
        }
        
        protected void createLeftAndRightBelts(WPIFactory factory, XPropertyManager propManager, InfluxDBWriter influxWriter) {
            leftBelt = new ShooterBeltSubsystem(
                    RobotSide.Left,
                    leftMotorIndex,
                    invertLeft,
                    invertLeftSensor,
                    factory, 
                    leftPIDValues,
                    propManager,
                    influxWriter);
            
            rightBelt = new ShooterBeltSubsystem(
                    RobotSide.Right,
                    rightMotorIndex,
                    invertRight,
                    invertRightSensor,
                    factory, 
                    rightPIDValues,
                    propManager,
                    influxWriter);
        }

        public ShooterBeltSubsystem getLeftBelt() {
            return leftBelt;
        }

        public ShooterBeltSubsystem getRightBelt() {
            return rightBelt;
        }
}