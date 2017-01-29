package competition.subsystems.agitator;

import org.junit.Test;

public class AgitatorSubsystemTest extends AgitatorTestBase{
        
    @Test
    public void stopTest(){
        agitator.stop();
        verifyAgitatorSetpoints(0);
    }
    
    @Test
    public void spinForwardsTest(){
        agitator.spinForwards();
        verifyAgitatorSetpoints(agitator.motorPowerProperty.get());
    }
    
    @Test
    public void spinBackwardsTest(){
        agitator.spinBackwards();
        verifyAgitatorSetpoints(agitator.motorPowerProperty.get() * -1);
    }
    
    @Test
    public void spinForwardsThenStopTest(){
        spinForwardsTest();
        stopTest();
    }

}
