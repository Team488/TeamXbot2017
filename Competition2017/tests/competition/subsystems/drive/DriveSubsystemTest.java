package competition.subsystems.drive;


import org.junit.Before;
import org.junit.Test;

public class DriveSubsystemTest extends DriveTestBase {
    
    @Before
    public void setUp() {        
        super.setUp();
    }
    
    @Test
    public void trivialTankDriveTest() {
        drive.tankDrivePowerMode(0.5, 0.5);
        
        verifyDriveSetpoints(0.5, 0.5);
    }
}
