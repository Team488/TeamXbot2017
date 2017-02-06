package competition.subsystems.feeder;

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

public class SideFeederSubsystem extends BaseSubsystem {

    private static Logger log = Logger.getLogger(SideFeederSubsystem.class);

    protected final XCANTalon feederMotor;

    private final RobotSide side;

    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;
    private final DoubleProperty feederCurrentSpeed;
    private final DoubleProperty feederTargetSpeed;
    private final DoubleProperty feederOutputPower;
    private final DoubleProperty feederTalonError;
    
    private final BooleanProperty atSpeedProp;
    private final BooleanProperty enableFeederLogging;

    private final DoubleProperty feederSpeedThresh;
    
    private final PIDPropertyManager pidProperties;

    public SideFeederSubsystem(int motor, RobotSide side, WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager){
        log.info("Creating " + side + " Feeder");

        this.side = side;
        
        this.pidProperties = new PIDPropertyManager("SideFeeder", propManager, assertionManager, 0, 0, 0, 0);
        
        feederSpeedThresh = propManager.createPersistentProperty(side + "Feeder nominal speed thresh (TPC)", motor);
        feederCurrentSpeed = propManager.createEphemeralProperty(side + "Feeder current speed (TPC)", 0);
        feederTargetSpeed = propManager.createEphemeralProperty(side + "Feeder goal speed (TPC)", 0);
        feederOutputPower = propManager.createEphemeralProperty(side + "Feeder voltage", 0);
        intakePowerProperty = propManager.createPersistentProperty("Feeder intake power", 0.5);
        ejectPowerProperty = propManager.createPersistentProperty("Feeder eject power", -0.5);

        atSpeedProp = propManager.createEphemeralProperty("Is" + side + " Feeder at speed?", false);
        feederTalonError = propManager.createEphemeralProperty(side + " Feeder speed error", 0);
        enableFeederLogging = propManager.createEphemeralProperty("Is " + side + " Feeder logging info?", false);

        this.feederMotor = factory.getCANTalonSpeedController(motor);

        setUpMotor(feederMotor);
        feederMotor.createTelemetryProperties(side + "Feeder motor", propManager);

        feederMotor.setBrakeEnableDuringNeutral(false);
        feederMotor.setProfile(0);
        feederMotor.setControlMode(TalonControlMode.PercentVbus);
    }

    private void setUpMotor(XCANTalon feeder) {
        log.info("Configuring " + side + " Feeder motor");
        feeder.setProfile(0);
        feeder.setFeedbackDevice(FeedbackDevice.QuadEncoder);

        log.info("Configuring voltages");
        feeder.configNominalOutputVoltage(0,  -0);
        feeder.configPeakOutputVoltage(12, -6);

        updateMotorPidValues(feeder);

        feeder.setControlMode(TalonControlMode.Speed);
        feeder.set(0);
    }

    private void updateMotorPidValues(XCANTalon motor) {
        motor.setP(pidProperties.getP());
        motor.setI(pidProperties.getI());
        motor.setD(pidProperties.getD());
        motor.setF(pidProperties.getF());
    }

    public void setFeederPower(double power) {
        feederMotor.ensureTalonControlMode(TalonControlMode.PercentVbus);
        feederMotor.set(power);
    }

    public void setFeederTargetSpeed(double speed) {
        feederMotor.ensureTalonControlMode(TalonControlMode.Speed);
        feederTargetSpeed.set(speed);

        updateMotorPidValues(feederMotor);
        feederMotor.set(speed);
    }

    public double getSpeed() {
        return feederMotor.getSpeed();
    }

    public boolean isAtSpeed() {
        return Math.abs(getSpeed() - feederTargetSpeed.get()) <= feederSpeedThresh.get();
    }

    public double getFeederTargetSpeed() {
        return feederTargetSpeed.get();
    }

    public void updateTelemetry() {
        feederMotor.updateTelemetryProperties();        

        atSpeedProp.set(isAtSpeed());

        feederCurrentSpeed.set(getSpeed());

        feederOutputPower.set(feederMotor.getOutputVoltage() / feederMotor.getBusVoltage());
        feederTalonError.set(feederMotor.getClosedLoopError());

        if(enableFeederLogging.get()){

            double currentTime = Timer.getFPGATimestamp();

            log.info(currentTime + "," 
                 + feederOutputPower.get() + "," 
                 + feederTalonError.get() + ","
                 + feederCurrentSpeed.get());
        }
    }

    public RobotSide getSide(){
        return side;
    }
    
    public void eject(){
        feederMotor.set(ejectPowerProperty.get());
    }
    
    public void intake(){
        feederMotor.set(intakePowerProperty.get());
    }
    
    public void stop(){
        feederMotor.set(0);
    }
}