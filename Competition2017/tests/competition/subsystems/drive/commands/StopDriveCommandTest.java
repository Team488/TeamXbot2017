package competition.subsystems.drive.commands;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import competition.subsystems.drive.DriveTestBase;

public class StopDriveCommandTest extends DriveTestBase {

    private StopDriveCommand stopCommand;
    
    @Before
    public void setUp() {
        super.setUp();
        stopCommand = injector.getInstance(StopDriveCommand.class);
    }
    
    @Test
    public void stopTest() {
        drive.tankDrivePowerMode(0.5, 0.75);
        verifyDriveSetpoints(0.5, 0.75);
        stopCommand.initialize();
        verifyDriveSetpoints(0, 0);
        assertTrue(stopCommand.isFinished());
    }
}
