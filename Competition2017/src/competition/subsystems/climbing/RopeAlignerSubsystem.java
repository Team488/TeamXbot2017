package competition.subsystems.climbing;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class RopeAlignerSubsystem extends BaseSubsystem {

    public final XCANTalon intakeMotor;
    private final DoubleProperty powerToMotor;
    
    @Inject
    public RopeAlignerSubsystem(WPIFactory factory, XPropertyManager propManager) {
        log.info("Creating");
        this.intakeMotor = factory.getCANTalonSpeedController(27);
        this.intakeMotor.setInverted(false);
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
