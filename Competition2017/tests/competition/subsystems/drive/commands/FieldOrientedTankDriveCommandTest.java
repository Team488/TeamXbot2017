package competition.subsystems.drive.commands;

import org.junit.Before;
import org.junit.Test;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveTestBase;
import xbot.common.controls.sensors.MockJoystick;

public class FieldOrientedTankDriveCommandTest extends DriveTestBase {

    private FieldOrientedTankDriveWithJoystick command;
    private OperatorInterface oi;
    
    @Before
    public void setUp() {
        super.setUp();
        command = injector.getInstance(FieldOrientedTankDriveWithJoystick.class);
        oi = injector.getInstance(OperatorInterface.class);
    }
    
    @Test
    public void goLeftStartingAt90DegreesTest() {
        setLeftJoystickXY(-1, 0);
        command.initialize();
        command.isFinished();
        command.execute();
        
        verifyDriveArcingLeft(0.5);
    }
    
    @Test
    public void goRightStartingAt90DegreesTest() {
        setLeftJoystickXY(1, 0);
        command.initialize();
        command.isFinished();
        command.execute();
        
        verifyDriveArcingRight(0.5);
    }
    
    @Test
    public void goStraightStartingAt90DegreesTest() {
        setLeftJoystickXY(0, 1);
        command.initialize();
        command.isFinished();
        command.execute();
        
        verifyDrivePositive();
    }
    
    @Test
    public void goBackLeftStartingAt90DegreesTest() {
        setLeftJoystickXY(-0.1, -1);
        command.initialize();
        command.isFinished();
        command.execute();
        
        verifyDriveArcingLeft(0.5);
    }
    
    @Test
    public void goBackRightStartingAt90DegreesTest() {
        setLeftJoystickXY(0.1, -1);
        command.initialize();
        command.isFinished();
        command.execute();
        
        verifyDriveArcingRight(0.5);
    }
    
    @Test
    public void okStartArcingLeftThenOnceOnTheTargetGoStraightTest() {
        setLeftJoystickXY(-1, 0);
        command.initialize();
        command.isFinished();
        command.execute();
        
        verifyDriveArcingLeft(0.5);
        pose.setCurrentHeading(-180);
        
        command.execute();
        
        verifyDrivePositive();
    }
    
    @Test
    public void okStartArcingRightThenOnceOnTheTargetGoStraightTest() {
        setLeftJoystickXY(1, 0);
        command.initialize();
        command.isFinished();
        command.execute();
        
        verifyDriveArcingRight(0.5);
        pose.setCurrentHeading(0);
        
        command.execute();
        
        verifyDrivePositive();
    }
    
    private void setLeftJoystickXY(double x, double y) {
        ((MockJoystick)oi.leftJoystick).setX(x);
        ((MockJoystick)oi.leftJoystick).setY(y);
    }
}
