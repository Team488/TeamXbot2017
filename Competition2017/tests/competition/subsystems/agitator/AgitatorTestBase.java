package competition.subsystems.agitator;

import static org.junit.Assert.assertEquals;

import xbot.common.injection.BaseWPITest;

public class AgitatorTestBase extends BaseWPITest{
    protected AgitatorSubsystem agitator;
    
    public void setUp() {        
        super.setUp();
        
        agitator = injector.getInstance(AgitatorSubsystem.class);
    }
    
    public void verifyAgitatorSetpoints(double power){
        assertEquals(power, agitator.agitatorMotor.get(), 0.001);
    }

}
