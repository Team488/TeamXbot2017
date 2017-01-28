package competition.subsystems.drive;


import org.junit.Before;
import org.junit.Test;

public class DriveSubsystemTest extends DriveTestBase {
    
    @Before
    public void setUp() {        
        super.setUp();
    }
    
    @Test
    public void directionalTankDriveTest(){
        drive.tankDrivePowerMode(-1, -1);
        verifyDriveSetpoints(-1, -1);
        
        drive.tankDrivePowerMode(1, 1);
        verifyDriveSetpoints(1, 1);
        
        drive.tankDrivePowerMode(-1, 1);
        verifyDriveSetpoints(-1, 1);
        
        drive.tankDrivePowerMode(1, -1);
        verifyDriveSetpoints(1, -1);
    } 
    
    @Test 
    public void highPowerInputTankDriveTest(){
        drive.tankDrivePowerMode(100, 100);
        verifyDriveSetpoints(1, 1);
        
        drive.tankDrivePowerMode(-100, -100);
        verifyDriveSetpoints(-1, -1);
        
        drive.tankDrivePowerMode(-100, 100);
        verifyDriveSetpoints(-1, 1);
        
        drive.tankDrivePowerMode(100, -100);
        verifyDriveSetpoints(1, -1);
    }
    
    @Test 
    public void lowPowerInputTankDriveTest(){
        drive.tankDrivePowerMode(0.01, 0.01);
        verifyDriveSetpoints(0.01, 0.01);
        
        drive.tankDrivePowerMode(-0.01, -0.01);
        verifyDriveSetpoints(-0.01, -0.01);
        
        drive.tankDrivePowerMode(-0.01, 0.01);
        verifyDriveSetpoints(-0.01, 0.01);
        
        drive.tankDrivePowerMode(0.01, -0.01);
        verifyDriveSetpoints(0.01, -0.01);
    }
}

