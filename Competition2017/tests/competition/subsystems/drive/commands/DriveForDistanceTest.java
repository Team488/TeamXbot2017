package competition.subsystems.drive.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.drive.DriveTestBase;

public class DriveForDistanceTest extends DriveTestBase {
 
    @Test
    public void positiveDistanceTest() {
        DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);
        command.setDeltaDistance(5);
        
        command.initialize();
        command.execute();
        
        verifyDrivePositive();
    }
    
    @Test
    public void negativeDistanceTest() {
        DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);
        command.setDeltaDistance(-5);
        
        command.initialize();
        command.execute();
        
        verifyDriveNegative();
    }
 
    @Test
    public void toleranceTest() {
        DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);

        command.initialize();
        command.setDeltaDistance(1.0);
        
        drive.rightDrive.setPosition(drive.convertInchesToTicks(-1));

        command.execute();
        assertTrue(!command.isFinished());
        
        drive.rightDrive.setPosition(drive.convertInchesToTicks(0.1));
        command.execute();
        for (int i = 0; i < 100; i++) {
            command.execute();
            
            if (command.isFinished()) {
                break;
            }
        }
        assertTrue(command.isFinished());
    }
}
