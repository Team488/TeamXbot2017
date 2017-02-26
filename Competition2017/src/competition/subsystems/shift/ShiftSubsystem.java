package competition.subsystems.shift;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.WPIFactory;

@Singleton
public class ShiftSubsystem extends BaseSubsystem {

    public final XSolenoid solenoid;
    public Gear gear;
    
    public enum Gear {
        LOW_GEAR, HIGH_GEAR
    }
    
    @Inject
    public ShiftSubsystem(WPIFactory factory) {
        log.info("Creating");
    
        this.solenoid = factory.getSolenoid(0);
        gear = Gear.LOW_GEAR;
    }
    
    /**
     * Sets the gear with the given gear
     * @param gear a low gear or high gear that determines which gear to trigger
     */
    public void setGear(Gear gear) {
        if (gear == Gear.LOW_GEAR) {
            solenoid.setOn(false);
        } else if (gear == Gear.HIGH_GEAR) {
            solenoid.setOn(true);
        } else {
            /* Defaults the gear to low just to be safe */
            solenoid.setOn(false);
        }
        this.gear = gear;
    }
    
    public Gear getGear() {
        return gear;
    }
}
