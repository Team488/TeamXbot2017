package competition.subsystems.shooter_wheel;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelCommand;
import competition.subsystems.shooter_wheel.commands.StopShooterCommand;
import xbot.common.properties.XPropertyManager;

public class ShooterWheelTest extends ShooterWheelTestBase {
    
     ShooterWheelSubsystem leftShooter;
     ShooterWheelSubsystem rightShooter;
     XPropertyManager propertyManager;
    
    @Before
    @Override
    public void setup() {
        super.setup();
        leftShooter = shooter.getLeftShooter();
        rightShooter = shooter.getRightShooter();
        propertyManager = injector.getInstance(XPropertyManager.class);
        
    }
    
    @Test
    public void testRunShooterCommand(){
        RunShooterWheelCommand rscLeft = new RunShooterWheelCommand(leftShooter, propertyManager);
        RunShooterWheelCommand rscRight = new RunShooterWheelCommand(rightShooter, propertyManager);
        
        rscLeft.initialize();
        rscLeft.execute();
        rscLeft.isFinished();
        
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
        StopShooterCommand stop = new StopShooterCommand(this.leftShooter);
                
        leftShooter.setPower(1);
        
        stop.initialize();
        stop.execute();
        stop.isFinished();
        
        verifyShooterPowers(0,0);
        
    }
}
