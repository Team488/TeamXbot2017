package competition.subsystems.climbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseTest;
import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class RopeAlignerSubsystemTest extends BaseTest {

    @Test
    public void intakeTest() {
        RopeAlignerSubsystem subsystem = injector.getInstance(RopeAlignerSubsystem.class);
        
        subsystem.intake();
            
        assertTrue(((MockCANTalon)subsystem.intakeMotor).getSetpoint() > 0);
    }
    
    @Test
    public void ejectTest() {
        RopeAlignerSubsystem subsystem = injector.getInstance(RopeAlignerSubsystem.class);
        
        subsystem.eject();
        
        assertTrue(((MockCANTalon)subsystem.intakeMotor).getSetpoint() < 0);
    }
    
    @Test
    public void stopTest() {
        RopeAlignerSubsystem subsystem = injector.getInstance(RopeAlignerSubsystem.class);
        
        subsystem.intake();
        subsystem.stopIntake();
        
        assertEquals(0, ((MockCANTalon)subsystem.intakeMotor).getSetpoint(), 0.000001);
    }
}
