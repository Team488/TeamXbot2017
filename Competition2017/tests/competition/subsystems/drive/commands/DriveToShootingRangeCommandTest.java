package competition.subsystems.drive.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.drive.DriveTestBase;
import edu.wpi.first.wpilibj.MockDistanceSensor;
import edu.wpi.first.wpilibj.MockDistanceSensorPair;
import edu.wpi.first.wpilibj.MockTimer;

public class DriveToShootingRangeCommandTest extends DriveTestBase {

    DriveToShootingRangeCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        
        command = injector.getInstance(DriveToShootingRangeCommand.class);
    }
    
    @Test
    public void testStartingClose() {
        setLidarDistance(0, 0);
        
        assertFalse(command.isFinished());
        command.initialize();
        command.execute();
        
        verifyDriveNegative();
    }
    
    @Test
    public void testStartingFar() {
        setLidarDistance(100, 100);
        
        assertFalse(command.isFinished());
        command.initialize();
        command.execute();
        
        verifyDrivePositive();
    }
    
    @Test
    public void testEnding() {
        setLidarDistance(30, 30);
        
        assertFalse(command.isFinished());
        command.initialize();
        command.execute();
        
        verifyDrivePositive();
        
        setLidarDistance(pose.getIdealShootingRange(), pose.getIdealShootingRange());
        
        // 1st run - error threshold met
        assertFalse(command.isFinished());
        command.execute();
        verifyStopped(0.1);
        
        // 2nd run - derivative threshold met
        assertFalse(command.isFinished());
        command.execute();

        // advance time for time threshold, time will stabilize
        MockTimer timer = injector.getInstance(MockTimer.class);
        timer.advanceTimeInSecondsBy(2);
        assertFalse(command.isFinished());
        command.execute(); 
        
        assertTrue(command.isFinished());
    }
    
    private void setLidarDistance(double a, double b) {
        ((MockDistanceSensor)((MockDistanceSensorPair)pose.getLidar()).getSensorA()).setDistance(a);
        ((MockDistanceSensor)((MockDistanceSensorPair)pose.getLidar()).getSensorB()).setDistance(b);
    }
}
