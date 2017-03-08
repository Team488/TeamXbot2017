package competition.subsystems.collector;

import static org.junit.Assert.assertEquals;

import competition.BaseTest;

public abstract class CollectorTestBase extends BaseTest {
    
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
