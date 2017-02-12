package competition.subsystems.agitator;

import org.apache.log4j.Logger;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
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

public class AgitatorSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(AgitatorSubsystem.class);
    protected final XCANTalon agitatorMotor;
    private final RobotSide side;
    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;
    protected final DoubleProperty agitatorTargetSpeed;
    private final DoubleProperty agitatorCurrentSpeed;
    private final DoubleProperty agitatorOutputPower;
    private final DoubleProperty agitatorTalonError;
    private final BooleanProperty atSpeedProp;
    private final BooleanProperty enableAgitatorLogging;
    private final DoubleProperty agitatorSpeedThresh;
    private final PIDPropertyManager pidProperties;

    public AgitatorSubsystem(int motor, RobotSide side, WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager){
        log.info("Creating " + side + " Agitator");

        this.side = side;
        this.pidProperties = new PIDPropertyManager("AgitatorSpeed", propManager, assertionManager, 0, 0, 0, 0);

        agitatorSpeedThresh = propManager.createPersistentProperty(side + "Agitator nominal speed thresh (TPC)", motor);
        agitatorCurrentSpeed = propManager.createEphemeralProperty(side + "Agitator current speed (TPC)", 0);
        agitatorTargetSpeed = propManager.createEphemeralProperty(side + "Agitator goal speed (TPC)", 0);
        agitatorOutputPower = propManager.createEphemeralProperty(side + "Agitator voltage", 0);
        intakePowerProperty = propManager.createPersistentProperty("Agitator intake power", 0.5);
        ejectPowerProperty = propManager.createPersistentProperty("Agitator eject power", -0.5);
        atSpeedProp = propManager.createEphemeralProperty("Is" + side + " Agitator at speed?", false);
        agitatorTalonError = propManager.createEphemeralProperty(side + " Agitator speed error", 0);
        enableAgitatorLogging = propManager.createEphemeralProperty("Is " + side + " Agitator logging info?", false);

        this.agitatorMotor = factory.getCANTalonSpeedController(motor);

        setUpMotor(agitatorMotor);
        agitatorMotor.createTelemetryProperties(side + "Agitator motor", propManager);
        agitatorMotor.setBrakeEnableDuringNeutral(false);
        agitatorMotor.setProfile(0);
        agitatorMotor.setControlMode(TalonControlMode.PercentVbus);
    }

    private void setUpMotor(XCANTalon agitatorMotor) {
        log.info("Configuring " + side + " Agitator motor");
        agitatorMotor.setProfile(0);
        agitatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);

        log.info("Configuring voltages");
        agitatorMotor.configNominalOutputVoltage(0,  -0);
        agitatorMotor.configPeakOutputVoltage(12, -12);

        updateMotorPidValues(agitatorMotor);
        agitatorMotor.setControlMode(TalonControlMode.Speed);
        agitatorMotor.set(0);
    }

    private void updateMotorPidValues(XCANTalon motor) {
        motor.setP(pidProperties.getP());
        motor.setI(pidProperties.getI());
        motor.setD(pidProperties.getD());
        motor.setF(pidProperties.getF());
    }

    public void setAgitatorPower(double power) {
        agitatorMotor.ensureTalonControlMode(TalonControlMode.PercentVbus);
        agitatorMotor.set(power);
    }

    public void setAgitatorTargetSpeed(double speed) {
        agitatorMotor.ensureTalonControlMode(TalonControlMode.Speed);
        agitatorTargetSpeed.set(speed);

        updateMotorPidValues(agitatorMotor);
        agitatorMotor.set(speed);
    }

    public double getSpeed() {
        return agitatorMotor.getSpeed();
    }
    
    public boolean isAtSpeed() {
        return Math.abs(getSpeed() - agitatorTargetSpeed.get()) <= agitatorSpeedThresh.get();
    }

    public double getAgitatorTargetSpeed() {
        return agitatorTargetSpeed.get();
    }
    
    public void updateTelemetry() {
        agitatorMotor.updateTelemetryProperties();        
        atSpeedProp.set(isAtSpeed());
        agitatorCurrentSpeed.set(getSpeed());
        agitatorOutputPower.set(agitatorMotor.getOutputVoltage() / agitatorMotor.getBusVoltage());
        agitatorTalonError.set(agitatorMotor.getClosedLoopError());

        if(enableAgitatorLogging.get()){
            double currentTime = Timer.getFPGATimestamp();

            log.info(currentTime + "," 
                 + agitatorOutputPower.get() + "," 
                 + agitatorTalonError.get() + ","
                 + agitatorCurrentSpeed.get());
        }
    }

    public RobotSide getSide(){
        return side;
    }

    public void eject(){
        agitatorMotor.set(ejectPowerProperty.get());
    }

    public void intake(){
        agitatorMotor.set(intakePowerProperty.get());
    }

    public void stop(){
        agitatorMotor.set(0);
    }
}
