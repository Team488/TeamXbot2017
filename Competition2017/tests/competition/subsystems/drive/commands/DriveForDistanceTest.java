package competition.subsystems.drive.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.DriveTestBase;
import xbot.common.controls.MockRobotIO;
import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.controls.sensors.MockGyro;

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
    
    @Test
    public void driveStraightTest(){
        MockRobotIO mockIO = injector.getInstance(MockRobotIO.class); 
        DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);
        
        mockIO.setGyroHeading(90);
        command.setDeltaDistance(10);
        
        command.initialize();
        command.execute();
        
        mockIO.setGyroHeading(80);
        command.execute();
        
        assertTrue(((MockCANTalon)drive.leftDrive).getSetpoint() < ((MockCANTalon)drive.rightDrive).getSetpoint()); 
        
        mockIO.setGyroHeading(100);
        command.execute();
        
        assertTrue(((MockCANTalon)drive.rightDrive).getSetpoint() < ((MockCANTalon)drive.leftDrive).getSetpoint()); 
    }
}
