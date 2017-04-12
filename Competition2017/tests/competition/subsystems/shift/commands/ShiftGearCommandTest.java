package competition.subsystems.shift.commands;

import org.junit.Test;

import competition.subsystems.shift.ShiftTestBase;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;


public class ShiftGearCommandTest extends ShiftTestBase {
    
    @Test
    public void lowGearTest() {
        ShiftGearCommand command = injector.getInstance(ShiftGearCommand.class);
        
        command.setGear(Gear.LOW_GEAR);
        command.initialize();
       
        verifyGear(Gear.LOW_GEAR);
    }
    
    @Test
    public void highGearTest() {
        ShiftGearCommand command = injector.getInstance(ShiftGearCommand.class);
        
        command.setGear(Gear.HIGH_GEAR);
        command.initialize();
        
        verifyGear(Gear.HIGH_GEAR);
    }
    
    @Test
    public void sameGearTest() {
        ShiftGearCommand command = injector.getInstance(ShiftGearCommand.class);
        
        command.setGear(Gear.LOW_GEAR);
        command.initialize();
        command.setGear(Gear.LOW_GEAR);
        command.initialize();
        
        verifyGear(Gear.LOW_GEAR);
    }
}
