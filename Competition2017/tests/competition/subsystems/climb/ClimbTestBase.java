package competition.subsystems.climb;

import xbot.common.injection.BaseWPITest;
import static org.junit.Assert.assertEquals;

public abstract class ClimbTestBase extends BaseWPITest {
    
    TestClimbingSubsystem climb;
    
    public void setUp() {        
        super.setUp();
        
        climb = injector.getInstance(TestClimbingSubsystem.class);
    }
    
    public void verifyClimbingSetpoints(double leftPower){ 

        assertEquals(leftPower,climb.getMotor().get(),0.001); 
    } 
}