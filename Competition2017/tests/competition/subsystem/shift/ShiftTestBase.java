package competition.subsystem.shift;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;

import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import edu.wpi.first.wpilibj.MockSolenoid;
import xbot.common.injection.BaseWPITest;

public abstract class ShiftTestBase extends BaseWPITest {
    
    protected ShiftSubsystem shiftSubsystem;
    
    public void setUp() {        
        super.setUp();
        
        shiftSubsystem = injector.getInstance(ShiftSubsystem.class);
    }
    
    public void verifyGear(Gear gear) {
        if (gear == Gear.LOW_GEAR) {
            assertFalse(((MockSolenoid)shiftSubsystem.solenoidLeft).get());
            assertFalse(((MockSolenoid)shiftSubsystem.solenoidRight).get());
        } else {
            assertTrue(((MockSolenoid)shiftSubsystem.solenoidLeft).get());
            assertTrue(((MockSolenoid)shiftSubsystem.solenoidRight).get());
        }
        
    }

    public void verifyShiftSameGear(Gear gear) {
        shiftSubsystem.setGear(gear);
        shiftSubsystem.setGear(gear);
        
        if (gear == Gear.LOW_GEAR) {
            assertFalse(((MockSolenoid)shiftSubsystem.solenoidLeft).get());
            assertFalse(((MockSolenoid)shiftSubsystem.solenoidRight).get());
        } else {
            assertTrue(((MockSolenoid)shiftSubsystem.solenoidLeft).get());
            assertTrue(((MockSolenoid)shiftSubsystem.solenoidRight).get());
        }
    }
}
