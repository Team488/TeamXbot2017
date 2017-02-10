package competition.subsystems.shooter;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class TestShooterSubsystem extends ShooterSubsystem {

    @Inject
    public TestShooterSubsystem(WPIFactory factory, XPropertyManager propManager) {
        super(factory, propManager);
    } 
    public Double getLeftShooterTargetSpeed(){
        return getLeftShooter().shooterTargetSpeed.get();
    }
    
    public Double getRightShooterTargetSpeed(){
        return getLeftShooter().shooterTargetSpeed.get();
    }
 
  
    

}
