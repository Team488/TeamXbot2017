package competition.subsystems.climbing.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.climbing.RopeAlignerSubsystem;
import xbot.common.controls.actuators.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class RopeAlignerCommandTest extends BaseTest {

    @Test
    public void executeTest() {
        RopeAlignerCommand command = injector.getInstance(RopeAlignerCommand.class);
        RopeAlignerSubsystem subsystem = injector.getInstance(RopeAlignerSubsystem.class);
        
        assertTrue(((MockCANTalon)subsystem.intakeMotor).get() == 0);
        
        command.initialize();
        command.execute();
        
        assertTrue(((MockCANTalon)subsystem.intakeMotor).getSetpoint() > 0);
    }
}
