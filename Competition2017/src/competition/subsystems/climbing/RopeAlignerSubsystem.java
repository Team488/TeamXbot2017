package competition.subsystems.climbing;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class RopeAlignerSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(RopeAlignerSubsystem.class);
    
    public final XCANTalon intakeMotor;
    private final DoubleProperty powerToMotor;
    
    @Inject
    public RopeAlignerSubsystem(WPIFactory factory, XPropertyManager propManager) {
        log.info("Creating Rope Aligner Subsystem");
        this.intakeMotor = factory.getCANTalonSpeedController(63);
        this.powerToMotor = propManager.createPersistentProperty("Rope aligner motor power", 0.1);
    }
    
    public void intake() {
        intakeMotor.set(powerToMotor.get());
    }
    
    public void eject() {
        intakeMotor.set(-powerToMotor.get());
    }
    
    public void stopIntake() {
        intakeMotor.set(0);
    }
}
