package competition.subsystems.agitator;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import xbot.common.injection.BaseWPITest;

public class AgitatorTestBase extends BaseWPITest{

    protected AgitatorSubsystem agitator;
    
    @Before
    public void setup() {
        super.setUp();
        
        agitator = injector.getInstance(TestAgitatorManagerSubsystem.class).getLeftAgitator();
    }
    
    public void verifyAgitatorSetpoints(double leftPower) {
        assertEquals(leftPower, agitator.agitatorMotor.get(), 0.001);
    }
}
