package competition.subsystems.shooter_wheel;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.commands.RunShooterCommand;
import competition.subsystems.shooter_wheel.commands.StopShooterCommand;

public class ShooterWheelTest extends ShooterWheelTestBase {
    
     ShooterWheelSubsystem leftShooter;
     ShooterWheelSubsystem rightShooter;
    
    @Before
    @Override
    public void setup() {
        super.setup();
        leftShooter = shooter.getLeftShooter();
        rightShooter = shooter.getRightShooter();
        
    }
    
    @Test
    public void testRunShooterCommand(){
        RunShooterCommand rscLeft = injector.getInstance(RunShooterCommand.class);
        RunShooterCommand rscRight = injector.getInstance(RunShooterCommand.class);
        
        rscLeft.setSide(leftShooter);
        rscLeft.initialize();
        rscLeft.execute();
        rscLeft.isFinished();
        
        rscRight.setSide(rightShooter);
        rscRight.initialize();
        rscRight.execute();
        rscRight.isFinished();
                
        assertTrue(leftShooter.getTargetSpeed() > 0);
        assertTrue(rightShooter.getTargetSpeed() > 0);
        
        assertTrue(shooter.getLeftShooter().getPower() > 0);
        assertTrue(shooter.getRightShooter().getPower() > 0);
    }
    
    @Test 
    public void testStopShooterCommand(){
        StopShooterCommand stop = injector.getInstance(StopShooterCommand.class);
        
        stop.setSide(leftShooter);
        
        leftShooter.setPower(1);
        
        stop.initialize();
        stop.execute();
        stop.isFinished();
        
        verifyShooterPowers(0,0);
        
    }
}
