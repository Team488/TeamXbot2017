package competition.subsystems.collector;

import org.apache.log4j.Logger;
import com.ctre.CANTalon.TalonControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
@Singleton
public class CollectorSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(CollectorSubsystem.class);
    
    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;
    
    protected final XCANTalon collectorMotor;
    
    @Inject
    public CollectorSubsystem(WPIFactory factory, XPropertyManager propManager){
        log.info("Creating Collector Subsystem");
        collectorMotor = factory.getCANTalonSpeedController(5);
        
        collectorMotor.setBrakeEnableDuringNeutral(false);
        
        collectorMotor.setProfile(0);
        collectorMotor.setControlMode(TalonControlMode.PercentVbus);
        
        ejectPowerProperty = propManager.createPersistentProperty("Collector eject power", -0.5);
        intakePowerProperty = propManager.createPersistentProperty("Collector intake power", 0.5);
        
    }
    
    public void stop(){
        collectorMotor.set(0);
    }
    
    public void intake(){
        collectorMotor.set(intakePowerProperty.get());
    }
    
    public void eject(){
        collectorMotor.set(ejectPowerProperty.get());
    }
}
