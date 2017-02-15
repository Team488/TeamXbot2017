package competition.subsystems.climb;

import org.junit.Test;
import competition.subsystems.climb.ClimbTestBase;

public class ClimbingSubsystemTest extends ClimbTestBase {
    
    @Test
    public void stopTest(){
        
        climb.ascend();
        verifyClimbingSetpoints(climb.getAscendProp().get());
        
        climb.stop();
        verifyClimbingSetpoints(0);
        
        climb.descend();
        verifyClimbingSetpoints(climb.getDescendProp().get());
        
        climb.stop();
        verifyClimbingSetpoints(0);
    }
    
    @Test
    public void ascendTest(){
        
        climb.ascend();     
        verifyClimbingSetpoints(climb.getAscendProp().get());
    }
   
    @Test
    public void descendTest(){
        
        climb.descend();   
        verifyClimbingSetpoints(climb.getDescendProp().get());
    }   
}