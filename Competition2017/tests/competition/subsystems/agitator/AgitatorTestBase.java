package competition.subsystems.agitator;

import static org.junit.Assert.assertEquals;
import org.junit.Before;

import competition.BaseTest;

public class AgitatorTestBase extends BaseTest{

    protected TestAgitatorManagerSubsystem agitatorManager;
    protected TestAgitatorSubsystem agitator;
    
    @Before
    public void setUp() {
        super.setUp();
        
        agitatorManager = injector.getInstance(TestAgitatorManagerSubsystem.class);
        agitator = (TestAgitatorSubsystem)agitatorManager.getLeftAgitator();
    }
    
    public void verifyAgitatorSetpoints(double leftPower) {
        assertEquals(leftPower, agitator.agitatorMotor.get(), 0.001);
    }
}
