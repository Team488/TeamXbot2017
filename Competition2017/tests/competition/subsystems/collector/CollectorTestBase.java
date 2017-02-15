package competition.subsystems.collector;


import xbot.common.injection.BaseWPITest;
import static org.junit.Assert.assertEquals;

public abstract class CollectorTestBase extends BaseWPITest {
    
    TestCollectorSubsystem collect;
    
    public void setUp() {        
        super.setUp();
        
        collect = injector.getInstance(TestCollectorSubsystem.class);
    }
    //Tests to see if the motor is running at the given power.
    public void verifyCollectSetpoints(double power){
        assertEquals(power,collect.collectorMotor.get(),0.001);
    }

}
