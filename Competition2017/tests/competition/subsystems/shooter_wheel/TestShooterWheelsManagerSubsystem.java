package competition.subsystems.shooter_wheel;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class TestShooterWheelsManagerSubsystem extends ShooterWheelsManagerSubsystem {

    @Inject
    public TestShooterWheelsManagerSubsystem(WPIFactory factory, XPropertyManager propManager) {
        super(factory, propManager);
    } 
    public Double getLeftShooterTargetSpeed(){
        return getLeftShooter().shooterTargetSpeed.get();
    }
    
    public Double getRightShooterTargetSpeed(){
        return getLeftShooter().shooterTargetSpeed.get();
    }
}
