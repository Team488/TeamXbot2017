package competition.subsystems.drive.commands;

import static org.junit.Assert.*;

import org.junit.Test;

import competition.subsystems.drive.DriveTestBase;

public class DriveToPositionCommandTest extends DriveTestBase {
 
    @Test
    public void positiveDistanceTest() {
        DriveToPositionCommand command = injector.getInstance(DriveToPositionCommand.class);
        command.setTargetPosition(5);
        
        command.initialize();
        command.execute();
        
        verifyDrivePositive();
    }
    
    @Test
    public void negativeDistanceTest() {
        DriveToPositionCommand command = injector.getInstance(DriveToPositionCommand.class);
        command.setTargetPosition(-5);
        
        command.initialize();
        command.execute();
        
        verifyDriveNegative();
    }
  
    @Test
    public void isFinishedTest() {
        DriveToPositionCommand command = injector.getInstance(DriveToPositionCommand.class);
        
        command.initialize();
        command.setTargetPosition(2.0);
        
        drive.leftDrive.setPosition(drive.convertInchesToTicks(1.1));
        drive.rightDrive.setPosition(drive.convertInchesToTicks(1.1));
        
        command.execute();
        
        assertTrue(command.isFinished());
    }
 
    @Test
    public void toleranceTest() {
        DriveToPositionCommand command = injector.getInstance(DriveToPositionCommand.class);

        command.initialize();
        command.setTargetPosition(1.0);
        
        drive.rightDrive.setPosition(drive.convertInchesToTicks(-1));

        command.execute();
        assertTrue(!command.isFinished());
        
        drive.rightDrive.setPosition(drive.convertInchesToTicks(0.1));
        command.execute();
        assertTrue(command.isFinished());
    }
}
