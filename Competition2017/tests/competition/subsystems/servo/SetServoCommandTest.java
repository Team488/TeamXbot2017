package competition.subsystems.servo;

import org.junit.Test;

import competition.BaseTest;
import edu.wpi.first.wpilibj.MockServo;

import static org.junit.Assert.assertEquals;

public class SetServoCommandTest extends BaseTest {
    
    @Test
    public void test(){
        SetServoCommand command = injector.getInstance(SetServoCommand.class);
        ShooterAimAdjustmentSubsystem subsystem = injector.getInstance(ShooterAimAdjustmentSubsystem.class);
        command.initialize();
        command.setTargetPose(0.4);
        command.execute();
        assertEquals(((MockServo)subsystem.leftServo).getValue(), ((0.4 * 0.6) + 0.2), 0.001);
        assertEquals(((MockServo)subsystem.rightServo).getValue(), 1 - ((0.4 * 0.6) + 0.2), 0.001);
    }

}
