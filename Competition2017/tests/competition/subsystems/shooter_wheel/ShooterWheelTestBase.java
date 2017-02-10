package competition.subsystems.shooter_wheel;

import xbot.common.injection.BaseWPITest;
import static org.junit.Assert.assertEquals;

import org.junit.Before;

public abstract class ShooterWheelTestBase extends BaseWPITest {
   
    protected TestShooterWheelsManagerSubsystem shooter;
    
    @Before
    public void setup() {
        super.setUp();
        
        shooter = injector.getInstance(TestShooterWheelsManagerSubsystem.class);
    }
    
    public void verifyShooterPowers(double leftPower, double rightPower) {
        assertEquals(leftPower, shooter.getLeftShooter().masterMotor.get(),0.001);
        assertEquals(rightPower, shooter.getRightShooter().masterMotor.get(),0.001);
    }
    
    public void verifyShooterSetSpeed(double leftTarget,double rightTarget) {
        assertEquals(leftTarget,shooter.getLeftShooterTargetSpeed(),0.001);
        assertEquals(rightTarget,shooter.getRightShooterTargetSpeed(),0.001);
    }
   
    
    
}
