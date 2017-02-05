package competition.subsystems.feeder;

import org.junit.Test;

public class FeederSubsystemTest extends FeederTestBase {

    @Test
    public void stopTest(){
        leftFeeder.stop();
        rightFeeder.stop();
        verifyFeederSetpoints(0, 0);
    }

    @Test
    public void intakeTest(){
        feeder.getLeftFeeder().intake();
        feeder.getRightFeeder().intake();
        verifyFeederSetpoints(
                leftFeeder.getIntakeProp().get(),
                rightFeeder.getIntakeProp().get());
    }
    
    @Test
    public void ejectTest(){
        feeder.getLeftFeeder().eject();
        feeder.getRightFeeder().eject();
        verifyFeederSetpoints(
                leftFeeder.getEjectProp().get(),
                rightFeeder.getEjectProp().get());
    }
}