package competition.subsystems.drive.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import competition.subsystems.drive.DriveTestBase;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.MockTimer;

public class DriveToPointUsingHeuristicsCommandTest extends DriveTestBase {

    DriveToPointUsingHeuristicsCommand command;
    MockTimer timer;
    
    @Before
    public void setUp() {
        super.setUp();
        command = injector.getInstance(DriveToPointUsingHeuristicsCommand.class);
        timer = injector.getInstance(MockTimer.class);
    }
    
    @Test
    public void driveAhead60inLeft60in() {
        pose.setCurrentHeading(90);
        
        command.setDeltaBasedTravel(60, 60, 90);
        command.initialize();
        assertFalse(command.isFinished());
        command.execute();
        
        verifyDrivePositive();
        
        // the robot then needs to move 30 units forward.
        driveDeltaEncoderInches(30, 30);
        
        assertFalse(command.isFinished());
        command.execute();
        
        verifyDrivePositive();
        verifyDriveArcingLeft(0.05);
        
        // lets force the robot 30 more units forward, so it's right on the line
        driveDeltaEncoderInches(30, 30);
        
        assertFalse(command.isFinished());
        command.execute();
        
        verifyTurningLeft();
        
        // turn the robot facing final travel direction
        setRobotHeading(180);
        
        assertFalse(command.isFinished());
        command.execute();
        
        verifyDrivePositive();
        verifyNotTurning(.05);
        
        // drive the robot forward to its final position
        driveDeltaEncoderInches(60, 60);
        
        assertFalse(command.isFinished());
        command.execute(); // after this line, error is small, but the derivative is large, and stabilization time is still needed
        
        verifyStopped(0.05);
        
        assertFalse(command.isFinished());
        command.execute(); // after this line, error is small, derivative is small, and stabilization has begun
        
        //advance time
        timer.advanceTimeInSecondsBy(1.5);
        assertFalse(command.isFinished());
        command.execute();  // after this line, error is small, derivative is small, and stabilization should have completed.
        
        assertTrue(command.isFinished());
    }
    
    @Test
    public void driveStraightWithSlightAngle() {
        pose.setCurrentHeading(90);
        
        command.setDeltaBasedTravel(0, 130, -10);
        command.initialize();
        assertFalse(command.isFinished());
        command.execute();
        
        verifyDrivePositive();
        
        driveDeltaEncoderInches(30, 30);
        
        assertFalse(command.isFinished());
        command.execute();
        
        verifyDrivePositive();
        verifyDriveArcingRight(0.05);
        
        driveDeltaEncoderInches(50, 50);
        
        assertFalse(command.isFinished());
        command.execute();
        
        verifyTurningRight();
        
        setRobotHeading(80);
        
        assertFalse(command.isFinished());
        command.execute();
        
        verifyStopped(1e-5);
        verifyNotTurning(.05);
        
        assertFalse(command.isFinished());
        command.execute(); // after this line, error is small, but the derivative is large, and stabilization time is still needed
        
        verifyStopped(1e-5);
        
        assertFalse(command.isFinished());
        command.execute(); // after this line, error is small, derivative is small, and stabilization has begun
        
        //advance time
        timer.advanceTimeInSecondsBy(1.5);
        assertFalse(command.isFinished());
        command.execute();  // after this line, error is small, derivative is small, and stabilization should have completed.
        
        assertTrue(command.isFinished());
    }
    
}
