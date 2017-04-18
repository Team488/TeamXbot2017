package competition.subsystems.shooter_wheel;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import competition.subsystems.shooter_wheel.commands.StopShooterCommand;
import xbot.common.properties.XPropertyManager;

public class ShooterWheelTest extends ShooterWheelTestBase {
    
     TestShooterWheelSubsystem leftShooter;
     TestShooterWheelSubsystem rightShooter;
     XPropertyManager propertyManager;
    
    @Before
    @Override
    public void setup() {
        super.setup();
        leftShooter = (TestShooterWheelSubsystem)shooter.getLeftShooter();
        rightShooter = (TestShooterWheelSubsystem)shooter.getRightShooter();
        propertyManager = injector.getInstance(XPropertyManager.class);
        
    }
    
    @Test
    public void testRunShooterCommand(){
        RunShooterWheelsForRangeCommand rscLeft = 
                new RunShooterWheelsForRangeCommand(TypicalShootingPosition.FlushToBoiler, leftShooter);
        RunShooterWheelsForRangeCommand rscRight = 
                new RunShooterWheelsForRangeCommand(TypicalShootingPosition.FlushToBoiler, rightShooter);
        
        rscLeft.initialize();
        rscLeft.execute();
        
        rscRight.initialize();
        rscRight.execute();
                
        assertTrue(leftShooter.getTargetSpeed() > 0);
        assertTrue(rightShooter.getTargetSpeed() > 0);
        
        assertTrue(shooter.getLeftShooter().getPower() > 0);
        assertTrue(shooter.getRightShooter().getPower() > 0);

        assertEquals(0, leftShooter.getAimServo().getValue(), 1e-5);
        assertEquals(1, rightShooter.getAimServo().getValue(), 1e-5);
    }
    
    @Test
    public void testRunShooterCommandLongRange(){
        RunShooterWheelsForRangeCommand rscLeft = 
                new RunShooterWheelsForRangeCommand(TypicalShootingPosition.OffsetFromHopper, leftShooter);
        RunShooterWheelsForRangeCommand rscRight = 
                new RunShooterWheelsForRangeCommand(TypicalShootingPosition.OffsetFromHopper, rightShooter);
        
        rscLeft.initialize();
        rscLeft.execute();
        
        rscRight.initialize();
        rscRight.execute();
                
        assertTrue(leftShooter.getTargetSpeed() > 0);
        assertTrue(rightShooter.getTargetSpeed() > 0);
        
        assertTrue(shooter.getLeftShooter().getPower() > 0);
        assertTrue(shooter.getRightShooter().getPower() > 0);

        assertEquals(1, leftShooter.getAimServo().getValue(), 1e-5);
        assertEquals(0, rightShooter.getAimServo().getValue(), 1e-5);
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
