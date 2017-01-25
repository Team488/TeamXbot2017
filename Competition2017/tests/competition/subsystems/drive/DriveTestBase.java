package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public abstract class DriveTestBase extends BaseWPITest {
    
    DriveSubsystem drive;
    
    public void setUp() {        
        super.setUp();
        
        drive = injector.getInstance(DriveSubsystem.class);
    }
    
    /**
     * Since the CANTalons do the actual power assignments on-board, we can't measure 
     * direct power output like we did in 2016. However, we can validate that the setpoint
     * we send to the CANTalon (whether a percentVBus value, or a speed value, etc...)
     * is what we expect it to be.
     * @param left Expected left setpoint
     * @param right Expected right setpoint
     */
    public void verifyDriveSetpoints(double left, double right) {
        assertEquals(left, ((MockCANTalon)drive.leftDrive).getSetpoint(), 0.001);
        assertEquals(right, ((MockCANTalon)drive.rightDrive).getSetpoint(), 0.001);
    }
}
