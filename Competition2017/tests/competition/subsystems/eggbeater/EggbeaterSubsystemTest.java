package competition.subsystems.eggbeater;

import org.junit.Test;

public class EggbeaterSubsystemTest extends EggbeaterTestBase{
        
    @Test
    public void stopTest(){
        agitator.stop();
        verifyEggbeaterSetpoints(0);
    }
    
    @Test
    public void spinForwardsTest(){
        agitator.spinForwards();
        verifyEggbeaterSetpoints(agitator.motorPowerProperty.get());
    }
    
    @Test
    public void spinBackwardsTest(){
        agitator.spinBackwards();
        verifyEggbeaterSetpoints(agitator.motorPowerProperty.get() * -1);
    }
    
    @Test
    public void spinForwardsThenStopTest(){
        spinForwardsTest();
        stopTest();
    }

}
