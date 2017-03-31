package competition.subsystems.shooter_wheel;

import org.junit.Before;

import competition.subsystems.shooter_wheel.commands.UnjamShooterWheelCommand;
import static org.junit.Assert.assertTrue;

public class UnjamShooterWheelTest extends ShooterWheelTestBase{
    private UnjamShooterWheelCommand command;
    
    @Before
    public void setup(){
        super.setup();
        command = injector.getInstance(UnjamShooterWheelCommand.class);
    }

    public void TestUnjamShooterWheel(){
        leftShooter.setTargetSpeed(0);
        leftShooter.setPower(1);
        command.execute();
        assertTrue(leftShooter.getPower() < 0);
    }

}
