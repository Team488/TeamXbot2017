package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import competition.subsystems.pose.PoseSubsystem;
import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public abstract class DriveTestBase extends BaseWPITest {
    
    protected DriveSubsystem drive;
    protected PoseSubsystem pose;
    
    public void setUp() {        
        super.setUp();
        
        drive = injector.getInstance(DriveSubsystem.class);
        pose = injector.getInstance(PoseSubsystem.class);
    }
    
    /**
     * Since the CANTalons do the actual power assignments on-board, we can't measure 
     * direct power output like we did in 2016. However, we can validate that the setpoint
     * we send to the CANTalon (whether a percentVBus value, or a speed value, etc...)
     * is what we expect it to be.
     * @param left Expected left setpoint
     * @param right Expected right setpoint
     */
    public void verifyDriveSetpoints(double left, double right) {
        assertEquals(left, ((MockCANTalon)drive.leftDrive).getSetpoint(), 0.001);
        assertEquals(right, ((MockCANTalon)drive.rightDrive).getSetpoint(), 0.001);
    }
    
    /**
     * This modifies the heading on the PoseSubsystem, not the raw gyro. However, unless you are testing
     * the PoseSubsystem directly, this is the most appropriate call to make.
     */
    public void setRobotHeading(double heading) {
        pose.setCurrentHeading(heading);
    }
        
    public void verifyDrivePositive() {
        assertTrue(((MockCANTalon)drive.leftDrive).getSetpoint() > 0);
        assertTrue(((MockCANTalon)drive.rightDrive).getSetpoint() > 0);
    }
    
    public void verifyDriveNegative() {
        assertTrue(((MockCANTalon)drive.leftDrive).getSetpoint() < 0);
        assertTrue(((MockCANTalon)drive.rightDrive).getSetpoint() < 0);
    }
}
