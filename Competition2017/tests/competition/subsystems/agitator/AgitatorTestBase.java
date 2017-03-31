package competition.subsystems.agitator;

import static org.junit.Assert.assertEquals;
import org.junit.Before;

import competition.BaseTest;

public class AgitatorTestBase extends BaseTest{

    protected AgitatorSubsystem agitator;
    
    @Before
    public void setUp() {
        super.setUp();
        
        agitator = injector.getInstance(TestAgitatorManagerSubsystem.class).getLeftAgitator();
    }
    
    public void verifyAgitatorSetpoints(double leftPower) {
        assertEquals(leftPower, agitator.agitatorMotor.get(), 0.001);
    }
}
