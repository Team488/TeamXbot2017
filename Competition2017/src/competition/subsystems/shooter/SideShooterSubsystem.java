package competition.subsystems.shooter;

import org.apache.log4j.Logger;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PID;
import xbot.common.math.PIDManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class SideShooterSubsystem extends BaseSubsystem {
    
    private static Logger log = Logger.getLogger(ShooterSubsystem.class);
    
    private final XCANTalon masterMotor;
    public enum ShooterSide {
        Left, 
        Right
    }
    
    private final ShooterSide side;
    
    // output telemetry properties
    private final DoubleProperty shooterTestingPowerStep ;
    private final DoubleProperty shooterCurrentSpeed;
    private final DoubleProperty shooterTargetSpeed;
    private final DoubleProperty shooterOutputPower;
    private final DoubleProperty shooterTalonError;
    private final BooleanProperty atSpeedProp;
    private final BooleanProperty enableShooterLogging;
    
    private final DoubleProperty p;
    private final DoubleProperty i;
    private final DoubleProperty d;
    private final DoubleProperty f;
    
    private final DoubleProperty shooterSpeedThresh;
    
   
    public SideShooterSubsystem(int motor, ShooterSide side, WPIFactory factory, XPropertyManager propManager){
        log.info("Creating " + side + " Shooter");
        this.side = side;
        
        p = propManager.createPersistentProperty(side + "Shooter wheel P", 0);
        i = propManager.createPersistentProperty(side + "Shooter wheel I", 0);
        d = propManager.createPersistentProperty(side + "Shooter wheel D", 0);
        f = propManager.createPersistentProperty(side + "Shooter wheel F", 0);
        
        // Reading through the manual, it looks like only a few things 
        // support human units. It might be easier to just keep everything in native units per 100ms,
        // which is what the Talon uses for all its calculations.
        // I call this Ticks per Deciseconds, or TPD
        shooterTestingPowerStep = propManager.createPersistentProperty(side +"Shooter power step", 0.025);
        shooterSpeedThresh = propManager.createPersistentProperty(side + "Shooter nominal speed thresh (TPC)", 1);
        shooterCurrentSpeed = propManager.createEphemeralProperty(side + "Shooter current speed (TPC)", 0);
        shooterTargetSpeed = propManager.createEphemeralProperty(side + "Shooter goal speed (TPC)", 0);
        shooterOutputPower = propManager.createEphemeralProperty(side + "Shooter voltage", 0);
        atSpeedProp = propManager.createEphemeralProperty("Is" + side + "Shooter at speed?", false);
        shooterTalonError = propManager.createEphemeralProperty(side + "Shooter speed error", 0);
        enableShooterLogging = propManager.createEphemeralProperty("Is " + side + "Shooter logging info?", false);
        
        this.masterMotor = factory.getCANTalonSpeedController(1);
        setUpMotorTeam(masterMotor);
        masterMotor.createTelemetryProperties(side + "Shooter master", propManager);
    }
    
    private void setUpMotorTeam(XCANTalon master) {
        // TODO: Check faults and voltage/temp/current
        log.info("Configuring " + side + "motor");
        // Master config
        master.setFeedbackDevice(FeedbackDevice.QuadEncoder);

        master.reverseSensor(true);
        master.setInverted(true);
        log.info("Configuring voltages");
        master.configNominalOutputVoltage(0,  -0);
        //PEAK OUTPUT VOLTAGE IS -6 because we do not want the the robot to be able
        //to go backward at 100% speed.
        master.configPeakOutputVoltage(12, -6);

        master.setProfile(0);
        master.setF(.099);
        updateMotorPidValues(master);
        master.setControlMode(TalonControlMode.Speed);

        master.set(0);
    }
    
    private void updateMotorPidValues(XCANTalon motor) {
        motor.setP(p.get());
        motor.setI(i.get());
        motor.setD(d.get());
        motor.setF(f.get());
    }
    
    /**
     * Sets the output power of the shooter directly - no PID of any kind
     * @param Power is set to shooter
     */
    public void setLauncherPower(double power) {
        masterMotor.ensureTalonControlMode(TalonControlMode.PercentVbus);
        masterMotor.set(power);
    }
    
    /**
     * Returns the last set motor power. Only works if the motor is currently in PercentVbus mode - otherwise, returns 0.
     * @return Last Motor power
     */
    public double getLauncherPower() {
        masterMotor.getOutputVoltage();
        double inversionFactor = masterMotor.getInverted() ? -1 : 1;
        
        return masterMotor.get() * inversionFactor;
    }

    /**
     * Gives the shooter a new speed goal.
     * @param shooterTargetSpeed in Rotations per Second (RPS)
     */
    public void setLauncherTargetSpeed(double speed) {
        masterMotor.ensureTalonControlMode(TalonControlMode.Speed);
        
        // Update property for dashboard
        shooterTargetSpeed.set(speed);
     // If there have been any changes to encoder settings or PID setings, apply them.
        updateMotorPidValues(masterMotor);
        // Instruct motor about new speed goal
        masterMotor.set(speed);
    }
    
    public double getSpeed() {
        return masterMotor.getSpeed();
    }
    
    public boolean isAtSpeed() {
        return Math.abs(getSpeed() - shooterTargetSpeed.get()) <= shooterSpeedThresh.get();
    }
    
    public double getLauncherTargetSpeed() {
        return shooterTargetSpeed.get();
    }
    
    /**
     * Updates driver station telemetry about the ShooterSubsystem. Try to call this once per scheduler pass-through.
     */
    public void updateTelemetry() {
        masterMotor.updateTelemetryProperties();        
        atSpeedProp.set(isAtSpeed());
        shooterCurrentSpeed.set(getSpeed());
        shooterOutputPower.set(masterMotor.getOutputVoltage() / masterMotor.getBusVoltage());
        shooterTalonError.set(masterMotor.getClosedLoopError());
        
        if(enableShooterLogging.get()){
            double currentTime = Timer.getFPGATimestamp();
            // voltage, speed, error. What's the best ordering of these things? 
            // voltage, error, speed. Ideally they would also be restricted to a constant number of digits
            // to prevent problems.
            
            // In the short term, we can pull log files off the RIO for processing, though it adds in
            // latency.
            
            // Format: time, voltage, error, speed
            log.info(currentTime + "," 
                 + shooterOutputPower.get() + "," 
                 + shooterTalonError.get() + ","
                 + shooterCurrentSpeed.get());
        }
    }
    public double getPowerStep(){
        return shooterTestingPowerStep.get();
    }
    public ShooterSide getSide(){
        return side;
    }
}

