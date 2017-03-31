package competition.subsystems.agitator;

import org.junit.Before;
import org.junit.Test;

import competition.subsystems.agitator.commands.UnjamAgitatorCommand;
import edu.wpi.first.wpilibj.MockTimer;
import xbot.common.controls.actuators.MockCANTalon;

public class UnjamAgitatorCommandTest extends AgitatorTestBase{
    private UnjamAgitatorCommand command;
    private MockTimer timer;
    
    @Before
    public void setUp(){
        super.setUp();
        command = new UnjamAgitatorCommand(agitator, propertyManager);
        timer = injector.getInstance(MockTimer.class);
    }
    
    @Test
    public void unjamAgitatorTest(){
        ((MockCANTalon)agitator.agitatorMotor).setOutputCurrent(100);
        command.execute();
        verifyAgitatorSetpoints(agitator.ejectPowerProperty.get());
        ((MockCANTalon)agitator.agitatorMotor).setOutputCurrent(3);
        timer.advanceTimeInSecondsBy(3);
        command.execute();
        verifyAgitatorSetpoints(agitator.ejectPowerProperty.get());
        timer.advanceTimeInSecondsBy(15);
        command.execute();
        verifyAgitatorSetpoints(agitator.intakePowerProperty.get());
    }

}
