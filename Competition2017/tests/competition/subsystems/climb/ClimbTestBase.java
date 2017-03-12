package competition.subsystems.climb;

import static org.junit.Assert.assertEquals;

import competition.BaseTest;

public abstract class ClimbTestBase extends BaseTest {
    
    TestClimbingSubsystem climb;
    
    public void setUp() {        
        super.setUp();
        
        climb = injector.getInstance(TestClimbingSubsystem.class);
    }
    
    public void verifyClimbingSetpoints(double leftPower){ 

        assertEquals(leftPower,climb.getMotor().get(),0.001); 
    } 
}