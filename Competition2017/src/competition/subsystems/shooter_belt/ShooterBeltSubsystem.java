package competition.subsystems.shooter_belt;

import org.apache.log4j.Logger;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import competition.subsystems.BaseXCANTalonSpeedControlledSubsystem;
import competition.subsystems.RobotSide;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShooterBeltSubsystem extends BaseXCANTalonSpeedControlledSubsystem {

    private final RobotSide side;
    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;

    public ShooterBeltSubsystem(
            RobotSide side,
            int masterChannel,
            boolean invertMaster,
            boolean invertMasterSensor,
            WPIFactory factory, 
            PIDPropertyManager pidPropertyManager,
            XPropertyManager propManager){
        super(
                side+"ShooterBelt",
                masterChannel,
                invertMaster,
                invertMasterSensor,
                factory,
                pidPropertyManager,
                propManager);

        this.side = side;
        
        intakePowerProperty = propManager.createPersistentProperty("ShooterBelt intake power", 0.5);
        ejectPowerProperty = propManager.createPersistentProperty("ShooterBelt eject power", -0.5);
    }

    public RobotSide getSide(){
        return side;
    }
    
    public void eject(){
        setPower(ejectPowerProperty.get());
    }
    
    public void intake(){
        setPower(intakePowerProperty.get());
    }
    
    public void stop(){
        setPower(0);
    }
}