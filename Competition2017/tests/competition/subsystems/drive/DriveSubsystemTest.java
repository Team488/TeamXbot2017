package competition.subsystems.drive;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.commands.TankDriveWithGamePadCommand;
import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.controls.sensors.XXboxController;
import xbot.common.controls.sensors.MockXboxControllerAdapter;

public class DriveSubsystemTest extends DriveTestBase {
    
    @Before
    public void setUp() {        
        super.setUp();
    }
    
    @Test
    public void tankDriveWithGamePadTest(){
       OperatorInterface oi = injector.getInstance(OperatorInterface.class);
       TankDriveWithGamePadCommand gamePad = injector.getInstance(TankDriveWithGamePadCommand.class);
       
       XXboxController xbox = oi.controller;
       ((MockXboxControllerAdapter)xbox).setLeftStick(0, -1);
       ((MockXboxControllerAdapter)xbox).setRightStick(0, -1);
       
       gamePad.initialize();
       gamePad.execute();
       verifyDriveSetpoints(1, 1);
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
    
    @Test
    public void testTankDriveVelocityInchesPerSec(){
        drive.tankDriveVelocityInchesPerSec(-100, 100);
        
        double leftTicksPerTenMs = drive.convertInchesToTicks(-100/100);
        double rightTicksPerTenMs = drive.convertInchesToTicks(100/100);
        
        verifyDriveSetpoints(leftTicksPerTenMs, rightTicksPerTenMs);
    }
}