package competition.subsystems.shooter_belt;

import org.junit.Test;

public class ShooterBeltSubsystemTest extends ShooterBeltTestBase {

    @Test
    public void stopTest(){
        leftBelt.stop();
        rightBelt.stop();
        verifyBeltSetpoints(0, 0);
    }

    @Test
    public void intakeTest(){
        beltsManager.getLeftBelt().intake();
        beltsManager.getRightBelt().intake();
        verifyBeltSetpoints(
                leftBelt.getIntakeProp().get(),
                rightBelt.getIntakeProp().get());
    }
    
    @Test
    public void ejectTest(){
        beltsManager.getLeftBelt().eject();
        beltsManager.getRightBelt().eject();
        verifyBeltSetpoints(
                leftBelt.getEjectProp().get(),
                rightBelt.getEjectProp().get());
    }
}