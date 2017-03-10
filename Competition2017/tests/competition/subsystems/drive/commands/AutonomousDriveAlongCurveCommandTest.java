package competition.subsystems.drive.commands;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.DriveTestBase;
import edu.wpi.first.wpilibj.MockTimer;

import org.junit.Before;
import org.junit.Test;

public class AutonomousDriveAlongCurveCommandTest extends DriveTestBase {
    MockTimer time;
    AutonomousDriveAlongPathCommand command;
    double[][] waypoints;
    DriveSubsystem drive;
    
    @Before
    public void setup() {
        time = injector.getInstance(MockTimer.class);
        command = injector.getInstance(AutonomousDriveAlongPathCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);

        waypoints = new double[][]{
            {0,100},
            {100,100},
            {200,100},
            {300,100}};
    }
    /*
     * Tests to see if DriveAlongCurve sets the right velocities to the motors;
     */
    @Test
    public void DriveAlongCurveVelocityTest(){
        command.setWayPoints(waypoints);
        
        command.initialize();
        command.execute();
        
        //eval
        verifyDriveSetpoints(drive.convertInchesToTicks(command.getPath().smoothLeftVelocity[0][1]),
                drive.convertInchesToTicks(command.getPath().smoothRightVelocity[0][1]/100));
        
        time.setTimeInSeconds(command.getPath().smoothLeftVelocity[1][0]);
        //run
        command.execute();
        
        verifyDriveSetpoints(drive.convertInchesToTicks(command.getPath().smoothLeftVelocity[1][1]/100),
                drive.convertInchesToTicks(command.getPath().smoothRightVelocity[1][1]/100));
    }
    /*
     * Tests to see if ending velocity becomes 0m/s
     */
    @Test
    public void DriveAlongCurveToEndTest(){
        command.setWayPoints(waypoints);
        command.initialize();
        
        for(int i = 0; i < command.getPath().smoothLeftVelocity.length; i++){
            time.setTimeInSeconds(command.getPath().smoothLeftVelocity[i][0]);
            command.isFinished();
            command.execute();
        }
        verifyDriveSetpoints(0, 0);
    }
}
