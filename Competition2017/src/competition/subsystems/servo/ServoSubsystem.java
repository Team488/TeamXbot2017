package competition.subsystems.servo;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XServo;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ServoSubsystem extends BaseSubsystem {
    public XServo servo;
    private DoubleProperty minPose;
    private DoubleProperty maxPose;
    private double deltaPose;
    
    @Inject
    public ServoSubsystem(WPIFactory factory, XPropertyManager propManager){
        servo = factory.getServo(5);
        
        minPose = propManager.createPersistentProperty(" minimum pose for servo", 0.2);
        maxPose = propManager.createPersistentProperty(" max pose for servo", 0.8);
        deltaPose = maxPose.get() - minPose.get();
    }
    
    public void setServo(double value){
        servo.set(value * deltaPose + minPose.get());
    }

}
