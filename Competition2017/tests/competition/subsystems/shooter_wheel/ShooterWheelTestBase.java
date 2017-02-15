package competition.subsystems.shooter_wheel;

import xbot.common.injection.BaseWPITest;
import static org.junit.Assert.assertEquals;

import org.junit.Before;

public abstract class ShooterWheelTestBase extends BaseWPITest {
   
    protected TestShooterWheelsManagerSubsystem shooter;
    protected TestShooterWheelSubsystem leftShooter;
    protected TestShooterWheelSubsystem rightShooter;
    
    @Before
    public void setup() {
        super.setUp();
        
        shooter = injector.getInstance(TestShooterWheelsManagerSubsystem.class);
        
        leftShooter = (TestShooterWheelSubsystem)shooter.getLeftShooter();
        rightShooter = (TestShooterWheelSubsystem)shooter.getRightShooter();
    }
    
    public void verifyShooterPowers(double leftPower, double rightPower) {
        assertEquals(leftPower, leftShooter.getMasterMotor().get(), 0.001);
        assertEquals(rightPower, rightShooter.getMasterMotor().get(), 0.001);
    }
    
    public void verifyShooterSetSpeed(double leftTarget,double rightTarget) {
        assertEquals(leftTarget,leftShooter.getTargetSpeed(), 0.001);
        assertEquals(rightTarget,rightShooter.getTargetSpeed(), 0.001);
    }
}
