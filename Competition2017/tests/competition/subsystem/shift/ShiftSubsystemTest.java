package competition.subsystem.shift;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import competition.subsystems.shift.ShiftSubsystem.Gear;

public class ShiftSubsystemTest extends ShiftTestBase {

    @Before
    public void setUp() {        
        super.setUp();
    }
    
    @Test
    public void setHighGearTest() {
        shiftSubsystem.setGear(Gear.HIGH_GEAR);
        verifyGear(Gear.HIGH_GEAR);
    }
    
    @Test
    public void setLowGearTest() {
        shiftSubsystem.setGear(Gear.LOW_GEAR);
        verifyGear(Gear.LOW_GEAR);
    }
    
    @Test
    public void sameGearTest() {
        verifyShiftSameGear(Gear.LOW_GEAR);
        verifyShiftSameGear(Gear.HIGH_GEAR);
    }
    
    @Test
    public void lowHighLowGearTest() {
        shiftSubsystem.setGear(Gear.LOW_GEAR);
        verifyGear(Gear.LOW_GEAR);
        shiftSubsystem.setGear(Gear.HIGH_GEAR);
        verifyGear(Gear.HIGH_GEAR);
        shiftSubsystem.setGear(Gear.LOW_GEAR);
        verifyGear(Gear.LOW_GEAR);
    }
    
    @Test
    public void getGearTest(){
        shiftSubsystem.setGear(Gear.LOW_GEAR);
        assertEquals(shiftSubsystem.getGear(), Gear.LOW_GEAR);
        shiftSubsystem.setGear(Gear.HIGH_GEAR);
        assertEquals(shiftSubsystem.getGear(), Gear.HIGH_GEAR);
    }
}
