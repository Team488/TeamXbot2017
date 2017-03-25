package competition.subsystems.shooter_wheel;

import static org.junit.Assert.*;

import org.junit.Test;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeVirtualThrottleCommand;
import xbot.common.math.PIDFactory;

public class RunShooterWheelsForRangeVirtualThrottleCommandTest extends ShooterWheelTestBase {

    @Test
    public void test() {
        RunShooterWheelsForRangeVirtualThrottleCommand command = new RunShooterWheelsForRangeVirtualThrottleCommand(
                TypicalShootingPosition.FlushToBoiler,
                this.leftShooter,
                injector.getInstance(PIDFactory.class)
                );
        
        command.initialize();
        
        command.execute();
        assertTrue(leftShooter.getMasterMotor().get() > 0);
        
    }

}
