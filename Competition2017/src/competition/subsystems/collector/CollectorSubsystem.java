package competition.subsystems.collector;

import com.ctre.CANTalon.TalonControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class CollectorSubsystem extends BaseSubsystem implements PeriodicDataSource {

    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;
    
    protected final XCANTalon collectorMotor;
    
    @Inject
    public CollectorSubsystem(WPIFactory factory, XPropertyManager propManager) {
        log.info("Creating");
        collectorMotor = factory.getCANTalonSpeedController(30);
        
        collectorMotor.setBrakeEnableDuringNeutral(false);
        
        collectorMotor.setProfile(0);
        collectorMotor.setControlMode(TalonControlMode.PercentVbus);
        collectorMotor.setInverted(true);
        
        ejectPowerProperty = propManager.createPersistentProperty("Collector eject power", -0.5);
        intakePowerProperty = propManager.createPersistentProperty("Collector intake power", 0.5);
        
        collectorMotor.createTelemetryProperties("Collector motor", propManager);
        
    }
    
    public void stop() {
        collectorMotor.set(0);
    }
    
    public void intake() {
        collectorMotor.set(intakePowerProperty.get());
    }
    
    public void eject() {
        collectorMotor.set(ejectPowerProperty.get());
    }

    @Override
    public void updatePeriodicData() {
        collectorMotor.updateTelemetryProperties();
    }
}
