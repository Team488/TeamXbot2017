package competition.subsystems.shooter_belt;

import static org.junit.Assert.assertEquals;

import competition.BaseTest;

public abstract class ShooterBeltTestBase extends BaseTest {

    protected TestShooterBeltsManagerSubsystem beltsManager;
    protected TestShooterBeltSubsystem leftBelt;
    protected TestShooterBeltSubsystem rightBelt;

    public void setUp() {        
        super.setUp();
        beltsManager = injector.getInstance(TestShooterBeltsManagerSubsystem.class);
        leftBelt = (TestShooterBeltSubsystem)beltsManager.getLeftBelt();
        rightBelt = (TestShooterBeltSubsystem)beltsManager.getRightBelt();
    }

    public void verifyBeltSetpoints(double leftPower, double rightPower){
       assertEquals(leftPower, leftBelt.getMotor().get(), 0.001);
       assertEquals(rightPower, rightBelt.getMotor().get(), 0.001);
    }
}