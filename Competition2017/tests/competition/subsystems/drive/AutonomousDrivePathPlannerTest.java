package competition.subsystems.drive;

import org.apache.log4j.Logger;
import org.junit.Test;

public class AutonomousDrivePathPlannerTest {
    
    private static Logger log = Logger.getLogger(AutonomousDrivePathPlanner.class);
    
    @Test
    public void testPathPlanner(){
        
        double[][] waypointPath = new double[][]{
            {0, 0},
            {100, 0}
        };
        
        
        
        AutonomousDrivePathPlanner planner = new AutonomousDrivePathPlanner(waypointPath);
        planner.calculate(5, 0.1, 3);
        
        for(int i=0; i<planner.smoothLeftVelocity.length; i++)
        {
            double timeStep = planner.smoothLeftVelocity[i][0];
            double leftVelocity = planner.smoothLeftVelocity[i][1];
            double rightVelocity = planner.smoothRightVelocity[i][1];
        
            System.out.println("Time step: " + timeStep +" Left velocity: " + leftVelocity + " Right velocity: " + rightVelocity);
        }
    }
}
