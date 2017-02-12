package competition.subsystems.shooter_wheel;

import org.apache.log4j.Logger;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import competition.subsystems.BaseXCANTalonSpeedControlledSubsystem;
import competition.subsystems.RobotSide;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShooterWheelSubsystem extends BaseXCANTalonSpeedControlledSubsystem {
    
    private static Logger log = Logger.getLogger(ShooterWheelSubsystem.class);
    private final RobotSide side;
   
    public ShooterWheelSubsystem(
            int masterChannel,
            int followerChannel,
            boolean masterInverted,
            boolean masterSensorInverted,
            boolean followerInverted,
            RobotSide side,
            PIDPropertyManager pidPropertyManager,
            WPIFactory factory,
            XPropertyManager propManager){
        super(
                side+"ShooterWheel",
                masterChannel,
                followerChannel,
                masterInverted,
                masterSensorInverted,
                followerInverted,
                factory,
                pidPropertyManager,
                propManager);
        
        this.side = side;
        
    }
    public RobotSide getSide(){
        return side;
    }
}

