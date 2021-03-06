package competition.subsystems.drive.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import competition.subsystems.drive.DriveTestBase;
import edu.wpi.first.wpilibj.MockTimer;
import xbot.common.controls.MockRobotIO;
import xbot.common.controls.actuators.MockCANTalon;

public class DriveForDistanceTest extends DriveTestBase {
    MockTimer mockTimer;

    @Before
    public void setUp() {
        super.setUp();
        mockTimer = injector.getInstance(MockTimer.class);
    }

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
        mockTimer.advanceTimeInSecondsBy(0.6);
        command.execute();
        assertTrue(command.isFinished());
    }

    @Test
    public void driveStraightTest() {
        MockRobotIO mockIO = injector.getInstance(MockRobotIO.class);
        DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);

        mockIO.setGyroHeading(90);
        command.setDeltaDistance(10);

        command.initialize();
        command.execute();

        mockIO.setGyroHeading(80);
        command.execute();

        assertTrue(((MockCANTalon) drive.leftDrive).getSetpoint() < ((MockCANTalon) drive.rightDrive).getSetpoint());

        mockIO.setGyroHeading(100);
        command.execute();

        assertTrue(((MockCANTalon) drive.rightDrive).getSetpoint() < ((MockCANTalon) drive.leftDrive).getSetpoint());
    }
}
