package competition.subsystems.agitator;

import org.junit.Test;

import xbot.common.controls.actuators.MockCANTalon;

public class AgitatorSubsystemTest extends AgitatorTestBase{
    
    @Test
    public void stopTest(){
        agitator.stop();
        verifyAgitatorSetpoints(0);
    }
    
    @Test
    public void agitatorIntakeTest(){
        agitator.intake();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());
    }
    
    @Test
    public void agitatorEjectTest(){
        agitator.eject();
        verifyAgitatorSetpoints(agitator.ejectPowerProperty.get());
    }
    
    @Test
    public void agitatorStopTest(){
        agitator.stop();
        verifyAgitatorSetpoints(0);
    }
    
    @Test
    public void agitatorStallTest() {
        MockCANTalon agitatorTalon = (MockCANTalon)agitator.agitatorMotor;
        agitatorTalon.setOutputCurrent(5);
        
        agitator.intake();
        agitator.updatePeriodicData();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());
        timer.setTimeInSeconds(100);
        
        agitatorTalon.setOutputCurrent(25);
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());

        timer.advanceTimeInSecondsBy(0.4);
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());

        timer.advanceTimeInSecondsBy(0.4);
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());

        timer.advanceTimeInSecondsBy(0.4);
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.getUnjamPower());

        agitatorTalon.setOutputCurrent(5);
        
        timer.advanceTimeInSecondsBy(0.4);
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.getUnjamPower());

        timer.advanceTimeInSecondsBy(0.4);
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.getUnjamPower());

        timer.advanceTimeInSecondsBy(0.4);
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());
    }
    
    @Test
    public void agitatorShortStallTest() {
        MockCANTalon agitatorTalon = (MockCANTalon)agitator.agitatorMotor;
        agitatorTalon.setOutputCurrent(5);
        
        agitator.intake();
        agitator.updatePeriodicData();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());
        timer.setTimeInSeconds(100);
        
        agitatorTalon.setOutputCurrent(25);
        
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());

        timer.advanceTimeInSecondsBy(0.4);
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());

        agitatorTalon.setOutputCurrent(5);
        
        timer.advanceTimeInSecondsBy(0.4);
        agitator.updatePeriodicData();
        agitator.intake();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());
    }

}