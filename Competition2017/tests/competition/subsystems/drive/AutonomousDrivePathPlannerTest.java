package competition.subsystems.drive;

import org.apache.log4j.Logger;
import org.junit.Test;

public class AutonomousDrivePathPlannerTest {
    
    private static Logger log = Logger.getLogger(AutonomousDrivePathPlanner.class);
    
    @Test
    public void testPathPlanner(){
        
        double[][] waypointPath = new double[][]{
            {0, 0},
            {1, 0}
        };
        
        
                
        AutonomousDrivePathPlanner planner = new AutonomousDrivePathPlanner(waypointPath);
        planner.calculate(5, 0.1, 3);
        
        AutonomousDrivePathPlanner velocity = new AutonomousDrivePathPlanner();
        velocity.velocityFix(smoothVelocity, origVelocity, tolerance);
        
        log.info("Smooth velocity: " + smoothVelocity);
        log.info("Original velocity: " + origVelocity);
        log.info("Tolerence: " + tolerence);
    }
}
