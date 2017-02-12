package competition.subsystems.shift;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.WPIFactory;

@Singleton
public class ShiftSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(ShiftSubsystem.class);
    
    public final XSolenoid solenoidLeft;
    public final XSolenoid solenoidRight;
    
    public enum Gear {
        LOW_GEAR, HIGH_GEAR
    }
    
    @Inject
    public ShiftSubsystem(WPIFactory factory) {
        log.info("Creating ShiftSubsystem");
        
        this.solenoidLeft = factory.getSolenoid(1);
        this.solenoidRight = factory.getSolenoid(2);
    }
    
    /**
     * Sets the gear with the given gear
     * @param gear a low gear or high gear that determines which gear to trigger
     */
    public void setGear(Gear gear) {
        if (gear == Gear.LOW_GEAR) {
            solenoidLeft.set(false);
            solenoidRight.set(false);
        } else if (gear == Gear.HIGH_GEAR) {
            solenoidLeft.set(true);
            solenoidRight.set(true);
        } else {
            /* Defaults the gear to low just to be safe */
            solenoidLeft.set(false);
            solenoidRight.set(false);
        }
    }
}
