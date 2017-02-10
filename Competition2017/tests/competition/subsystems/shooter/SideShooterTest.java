package competition.subsystems.shooter;

import org.junit.Before;
import org.junit.Test;

import competition.subsystems.shooter.commands.ContinueShooterCommand;
import competition.subsystems.shooter.commands.SetShooterSpeedCommand;
import competition.subsystems.shooter.commands.StepShooterPowerCommand;
import competition.subsystems.shooter.commands.StopShooterCommand;

public class SideShooterTest extends ShooterTestBase {
    
     SideShooterSubsystem leftShooter;
     SideShooterSubsystem rightShooter;
    
    @Before
    @Override
    public void setup() {
        super.setup();
        leftShooter = shooter.getLeftShooter();
        rightShooter = shooter.getRightShooter();
        
    }
    
    @Test
    public void testContinueShooterCommand(){
        ContinueShooterCommand cmdLeft = injector.getInstance(ContinueShooterCommand.class);
        ContinueShooterCommand cmdRight = injector.getInstance(ContinueShooterCommand.class);
        
        cmdLeft.setSide((leftShooter));
        cmdLeft.initialize();
        cmdLeft.execute();
        cmdLeft.isFinished();
        
        cmdRight.setSide(rightShooter);
        cmdRight.initialize();
        cmdRight.execute();
        cmdRight.isFinished();
        
         verifyShooterPowers(shooter.getLeftShooter().getLauncherPower(), shooter.getRightShooter().getLauncherPower());;
    }
    
    @Test
    public void testStepShooterPowerCommand(){
        StepShooterPowerCommand sspc = injector.getInstance(StepShooterPowerCommand.class);
        sspc.setSide(leftShooter);
        
        double leftTemp = shooter.getLeftShooter().getLauncherPower();
        
        sspc.initialize();
        sspc.execute();
        sspc.isFinished();
        
        verifyShooterPowers(leftTemp + leftShooter.getPowerStep(), 0);
    }
    
    @Test 
    public void testStopShooterCommand(){
        StopShooterCommand stop = injector.getInstance(StopShooterCommand.class);
        StepShooterPowerCommand sspc = injector.getInstance(StepShooterPowerCommand.class);
        
        sspc.setSide(leftShooter);
        stop.setSide(leftShooter);
        
        sspc.initialize();
        sspc.execute();
        sspc.isFinished();
        
        stop.initialize();
        stop.execute();
        stop.isFinished();
        
        verifyShooterPowers(0,0);
        
    }
    
    @Test
    public void testSetShooterSpeedCommand(){
        SetShooterSpeedCommand ssscLeft = injector.getInstance(SetShooterSpeedCommand.class);
        SetShooterSpeedCommand ssscRight = injector.getInstance(SetShooterSpeedCommand.class);
        ssscLeft.setSide(leftShooter);
        
        ssscLeft.initialize();
        ssscLeft.execute();
        ssscLeft.isFinished();
        
        ssscRight.setSide(rightShooter);
        
        ssscRight.initialize();
        ssscRight.execute();
        ssscRight.isFinished();
       
        verifyShooterSetSpeed(leftShooter.shooterTargetSpeed.get(), rightShooter.shooterTargetSpeed.get());
    }
    
    
   
    

}
