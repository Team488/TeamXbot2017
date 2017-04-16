package competition.subsystems.servo;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XServo;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ShooterAimAdjustmentSubsystem extends BaseSubsystem {
    public final XServo leftServo;
    public final XServo rightServo;
    private final DoubleProperty minPose;
    private final DoubleProperty maxPose;
    
    @Inject
    public ShooterAimAdjustmentSubsystem(WPIFactory factory, XPropertyManager propManager){
        leftServo = factory.getServo(1);
        rightServo = factory.getServo(2);
        minPose = propManager.createPersistentProperty("Minimum pose for servo", 0.2);
        maxPose = propManager.createPersistentProperty("Max pose for servo", 0.8);
    }
    
    public void setServo(double value){
        double deltaPose = maxPose.get() - minPose.get();
        double scaledValue = value * deltaPose + minPose.get();
        leftServo.set(scaledValue);
        rightServo.set(1 - scaledValue);
    }

}
