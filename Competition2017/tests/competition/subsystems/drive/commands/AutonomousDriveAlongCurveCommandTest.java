package competition.subsystems.drive.commands;

import competition.subsystems.drive.DriveTestBase;
import edu.wpi.first.wpilibj.MockTimer;
import edu.wpi.first.wpilibj.Timer;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AutonomousDriveAlongCurveCommandTest extends DriveTestBase {
    MockTimer time;
    
    @Before
    public void setup() {
        time = injector.getInstance(MockTimer.class);
    }
    @Test
    public void DriveInStraightLineTest(){
        AutonomousDriveAlongCurveCommand command = injector.getInstance(AutonomousDriveAlongCurveCommand.class);
        double[][] waypoints = new double[][]{
            {0,1},
            {1,1},
            {2,1},
            {3,1}};
        command.setWayPoints(waypoints);
        
        command.initialize();
        command.isFinished();
        command.execute();
        
        //eval
        verifyDriveSetpoints(command.getPath().smoothLeftVelocity[0][1],
                command.getPath().smoothRightVelocity[0][1]);
        time.setTimeInSeconds(command.getPath().smoothLeftVelocity[1][0]);
        //run
        command.isFinished();
        command.execute();
        
        verifyDriveSetpoints(command.getPath().smoothLeftVelocity[1][1],
                command.getPath().smoothRightVelocity[1][1]);
        
    }
        
        
    }

