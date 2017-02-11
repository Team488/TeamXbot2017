package competition.subsystems.shooter_belt;

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

public class ShooterBeltSubsystem extends BaseSubsystem {

    private static Logger log = Logger.getLogger(ShooterBeltSubsystem.class);

    protected final XCANTalon beltMotor;

    private final RobotSide side;

    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;
    private final DoubleProperty beltCurrentSpeed;
    private final DoubleProperty beltTargetSpeed;
    private final DoubleProperty beltOutputPower;
    private final DoubleProperty beltTalonError;
    
    private final BooleanProperty atSpeedProp;
    private final BooleanProperty enableBeltLogging;

    private final DoubleProperty beltSpeedThresh;
    
    private final PIDPropertyManager pidProperties;

    public ShooterBeltSubsystem(int motor, RobotSide side, WPIFactory factory, XPropertyManager propManager, RobotAssertionManager assertionManager){
        log.info("Creating " + side + " ShooterBelt");

        this.side = side;
        
        this.pidProperties = new PIDPropertyManager("ShooterBeltSpeed", propManager, assertionManager, 0, 0, 0, 0);
        
        beltSpeedThresh = propManager.createPersistentProperty(side + "ShooterBelt nominal speed thresh (TPC)", motor);
        beltCurrentSpeed = propManager.createEphemeralProperty(side + "ShooterBelt current speed (TPC)", 0);
        beltTargetSpeed = propManager.createEphemeralProperty(side + "ShooterBelt goal speed (TPC)", 0);
        beltOutputPower = propManager.createEphemeralProperty(side + "ShooterBelt voltage", 0);
        intakePowerProperty = propManager.createPersistentProperty("ShooterBelt intake power", 0.5);
        ejectPowerProperty = propManager.createPersistentProperty("ShooterBelt eject power", -0.5);

        atSpeedProp = propManager.createEphemeralProperty("Is" + side + " ShooterBelt at speed?", false);
        beltTalonError = propManager.createEphemeralProperty(side + " ShooterBelt speed error", 0);
        enableBeltLogging = propManager.createEphemeralProperty("Is " + side + " ShooterBelt logging info?", false);

        this.beltMotor = factory.getCANTalonSpeedController(motor);

        setUpMotor(beltMotor);
        beltMotor.createTelemetryProperties(side + "ShooterBelt motor", propManager);

        beltMotor.setBrakeEnableDuringNeutral(false);
        beltMotor.setProfile(0);
        beltMotor.setControlMode(TalonControlMode.PercentVbus);
    }

    private void setUpMotor(XCANTalon beltMotor) {
        log.info("Configuring " + side + " ShooterBelt motor");
        beltMotor.setProfile(0);
        beltMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);

        log.info("Configuring voltages");
        beltMotor.configNominalOutputVoltage(0,  -0);
        beltMotor.configPeakOutputVoltage(12, -6);

        updateMotorPidValues(beltMotor);

        beltMotor.setControlMode(TalonControlMode.Speed);
        beltMotor.set(0);
    }

    private void updateMotorPidValues(XCANTalon motor) {
        motor.setP(pidProperties.getP());
        motor.setI(pidProperties.getI());
        motor.setD(pidProperties.getD());
        motor.setF(pidProperties.getF());
    }

    public void setBeltPower(double power) {
        beltMotor.ensureTalonControlMode(TalonControlMode.PercentVbus);
        beltMotor.set(power);
    }

    public void setBeltTargetSpeed(double speed) {
        beltMotor.ensureTalonControlMode(TalonControlMode.Speed);
        beltTargetSpeed.set(speed);

        updateMotorPidValues(beltMotor);
        beltMotor.set(speed);
    }

    public double getSpeed() {
        return beltMotor.getSpeed();
    }

    public boolean isAtSpeed() {
        return Math.abs(getSpeed() - beltTargetSpeed.get()) <= beltSpeedThresh.get();
    }

    public double getBeltTargetSpeed() {
        return beltTargetSpeed.get();
    }

    public void updateTelemetry() {
        beltMotor.updateTelemetryProperties();        

        atSpeedProp.set(isAtSpeed());

        beltCurrentSpeed.set(getSpeed());

        beltOutputPower.set(beltMotor.getOutputVoltage() / beltMotor.getBusVoltage());
        beltTalonError.set(beltMotor.getClosedLoopError());

        if(enableBeltLogging.get()){

            double currentTime = Timer.getFPGATimestamp();

            log.info(currentTime + "," 
                 + beltOutputPower.get() + "," 
                 + beltTalonError.get() + ","
                 + beltCurrentSpeed.get());
        }
    }

    public RobotSide getSide(){
        return side;
    }
    
    public void eject(){
        beltMotor.set(ejectPowerProperty.get());
    }
    
    public void intake(){
        beltMotor.set(intakePowerProperty.get());
    }
    
    public void stop(){
        beltMotor.set(0);
    }
    public double getTargetBeltSpeed(){
        return beltTargetSpeed.get();
    }
}