package competition.subsystems.climbing;

import org.apache.log4j.Logger;
import com.ctre.CANTalon.TalonControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ClimbingSubsystem extends BaseSubsystem {

    private static Logger log = Logger.getLogger(ClimbingSubsystem.class);

    protected final DoubleProperty ascendPowerProperty;
    protected final DoubleProperty descendPowerProperty;
    protected final XCANTalon climbingMotor;

    public final XSolenoid brakeSolenoid;

    @Inject
    public ClimbingSubsystem(WPIFactory factory, XPropertyManager propManager){

        log.info("Creating Climbing Subsystem");

        climbingMotor = factory.getCANTalonSpeedController(25);
        climbingMotor.setProfile(0);
        climbingMotor.setBrakeEnableDuringNeutral(true);
        climbingMotor.setControlMode(TalonControlMode.PercentVbus);

        brakeSolenoid = factory.getSolenoid(3);
        
        descendPowerProperty = propManager.createPersistentProperty("Climber descend power", -0.5);
        ascendPowerProperty = propManager.createPersistentProperty("Climber ascend power", 0.5);
    }

    public void stop() {
        climbingMotor.set(0);
        brakeSolenoid.set(true);
    }

    public void ascend() {
        climbingMotor.set(ascendPowerProperty.get());
        brakeSolenoid.set(false);
    }

    public void descend() {
        climbingMotor.set(descendPowerProperty.get());
        brakeSolenoid.set(false);
    }
    
    public void setBrake(boolean brakeOn) {
        brakeSolenoid.set(brakeOn);
    }
}