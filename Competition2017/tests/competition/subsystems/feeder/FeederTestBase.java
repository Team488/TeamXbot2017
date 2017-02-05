package competition.subsystems.feeder;

import xbot.common.injection.BaseWPITest;
import static org.junit.Assert.assertEquals;

public abstract class FeederTestBase extends BaseWPITest {

    protected TestFeederSubsystem feeder;
    protected TestSideFeederSubsystem leftFeeder;
    protected TestSideFeederSubsystem rightFeeder;

    public void setUp() {        
        super.setUp();
        feeder = injector.getInstance(TestFeederSubsystem.class);
        leftFeeder = (TestSideFeederSubsystem)feeder.getLeftFeeder();
        rightFeeder = (TestSideFeederSubsystem)feeder.getRightFeeder();
    }

    public void verifyFeederSetpoints(double leftPower, double rightPower){
       assertEquals(leftPower,leftFeeder.getMotor().get(),0.001);
       assertEquals(rightPower,rightFeeder.getMotor().get(),0.001);
    }
}