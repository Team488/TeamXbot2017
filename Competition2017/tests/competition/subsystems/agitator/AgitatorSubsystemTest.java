package competition.subsystems.agitator;

import org.junit.Test;

public class AgitatorSubsystemTest extends AgitatorTestBase{
    
    @Test
    public void stopTest(){
        agitator.stop();
        verifyAgitatorSetpoints(0);
    }
    
    @Test
    public void agitatorIntakeTest(){
        agitator.intake();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());
    }
    
    @Test
    public void agitatorEjectTest(){
        agitator.eject();
        verifyAgitatorSetpoints(agitator.ejectPowerProperty.get());
    }
    
    @Test
    public void agitatorStopTest(){
        agitator.stop();
        verifyAgitatorSetpoints(0);
    }
    
}