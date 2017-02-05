package competition.subsystems.drive.commands;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import competition.subsystems.drive.DriveTestBase;
import xbot.common.controls.actuators.MockCANTalon;

public class RotateToHeadingCommandTest extends DriveTestBase {

    RotateToHeadingCommand command;
    
    @Before
    public void setup() {
        
        super.setUp();
        
        command = injector.getInstance(RotateToHeadingCommand.class);
    }
    
    @Test
    public void testTurnLeft90() {
 
        setRobotHeading(0);
        command.setTargetHeading(90);
 
        command.initialize();
        command.execute();
        
        setRobotHeading(90);
        // first execute, the "error is small" condition is hit
        command.execute();
        // second execute, the "derivative is small" condition is hit, so it will return true.
        command.execute();
        assertTrue(command.isFinished());
    }
    
    @Test
    public void testTurnRight90From150() {
 
        setRobotHeading(150);
        command.setTargetHeading(90);
 
        command.initialize();
        command.execute();
        
        assertTrue(((MockCANTalon)drive.leftDrive).getSetpoint() > 0);
        assertTrue(((MockCANTalon)drive.rightDrive).getSetpoint() < 0);
    }
    
    @Test
    public void testTurnLeft90FromNeg90() {
 
        setRobotHeading(-90);
        command.setTargetHeading(90);
 
        command.initialize();
        command.execute();
        
        assertTrue(((MockCANTalon)drive.leftDrive).getSetpoint() < 0);
        assertTrue(((MockCANTalon)drive.rightDrive).getSetpoint() > 0);
    }
    
    @Test
    public void testTurnRight90FromNeg150() {
        
        setRobotHeading(-150);
        command.setTargetHeading(90);
 
        command.initialize();
        command.execute();
        
        assertTrue(((MockCANTalon)drive.leftDrive).getSetpoint() > 0);
        assertTrue(((MockCANTalon)drive.rightDrive).getSetpoint() < 0);
    }
    
}
