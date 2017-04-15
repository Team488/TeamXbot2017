package competition.subsystems.servo;

import org.junit.Test;

import competition.BaseTest;
import edu.wpi.first.wpilibj.MockServo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SetServoCommandTest extends BaseTest {
    
    @Test
    public void test(){
        SetServoCommand command = injector.getInstance(SetServoCommand.class);
        ServoSubsystem subsystem = injector.getInstance(ServoSubsystem.class);
        command.setTargetPose(0.4);
        command.execute();
        assertEquals(((MockServo)subsystem.servo).getValue(), ((0.4 * 0.6) + 0.2), 0.001);
    }

}
