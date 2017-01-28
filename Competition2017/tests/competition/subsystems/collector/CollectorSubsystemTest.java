package competition.subsystems.collector;

import org.junit.Test;


public class CollectorSubsystemTest extends CollectorTestBase {
    
   
    @Test
    public void stopTest(){
        collect.stop();
        
        verifyCollectSetpoints(0);
    }
    
    @Test
    public void intakeTest(){
        collect.intake();
        
        verifyCollectSetpoints(collect.getIntakeProp().get());
    }
    
    @Test
    public void ejectTest(){
        collect.eject();
        
        verifyCollectSetpoints(collect.getEjectProp().get());
    }
    
}
