package competition.subsystems.collector;

import org.junit.Test;

import competition.subsystems.collector.CollectorSubsystem.Power;

public class CollectorSubsystemTest extends CollectorTestBase {
    
   
    @Test
    public void stopTest(){
        collect.stop();
        
        verifyCollectSetpoints(0);
    }
    
    @Test
    public void intakeTest(){
        collect.intake(Power.LOW);
        
        verifyCollectSetpoints(collect.getLowIntakeProp().get());
    }
    
    @Test
    public void ejectTest(){
        collect.eject();
        
        verifyCollectSetpoints(collect.getEjectProp().get());
    }
    
}
