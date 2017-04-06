package competition.subsystems.agitator;

import com.ctre.CANTalon.TalonControlMode;

import competition.subsystems.RobotSide;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class AgitatorSubsystem extends BaseSubsystem implements PeriodicDataSource {

    protected final XCANTalon agitatorMotor;
    private final RobotSide side;
    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;
    
    private boolean isIntaking = false;

    protected DoubleProperty agitatorOverCurrentThreshold;
    protected DoubleProperty agitatorStallDuration;
    protected DoubleProperty unjamDuration;
    protected DoubleProperty unjamPower;
    private boolean isTrackingOverCurrent = false;
    private double overCurrentStart = 0;
    
    private double unjamEnd = -1;

    public AgitatorSubsystem(
            int motor,
            RobotSide side,
            boolean invertMotor,
            WPIFactory factory, 
            XPropertyManager propManager, 
            RobotAssertionManager assertionManager){
        super(side + " Agitator");

        this.side = side;
        intakePowerProperty = propManager.createPersistentProperty("Agitator intake power", 0.5);
        ejectPowerProperty = propManager.createPersistentProperty("Agitator eject power", -0.5);

        agitatorOverCurrentThreshold = propManager.createPersistentProperty("Agitator over-current threshold", 20);
        agitatorStallDuration = propManager.createPersistentProperty("Agitator stall duration threshold", 1);

        unjamDuration = propManager.createPersistentProperty("Agitator unjam duration", 1);
        unjamPower = propManager.createPersistentProperty("Agitator unjam power", -0.7);
        
        this.agitatorMotor = factory.getCANTalonSpeedController(motor);

        agitatorMotor.setBrakeEnableDuringNeutral(false);
        agitatorMotor.setProfile(0);
        agitatorMotor.setInverted(invertMotor);
        agitatorMotor.setControlMode(TalonControlMode.PercentVbus);
        
        this.agitatorMotor.createTelemetryProperties(side.toString(), propManager);
    }

    protected void setAgitatorPowerRaw(double power) {
        agitatorMotor.ensureTalonControlMode(TalonControlMode.PercentVbus);
        agitatorMotor.set(power);
    }
    
    public void setAgitatorPower(double power) {
        setAgitatorPowerRaw(power);
        unjamEnd = -1;
        isIntaking = false;
    }
  
    public RobotSide getSide() {
        return side;
    }

    public void eject() {
        setAgitatorPower(ejectPowerProperty.get());
    }

    public void intake() {
        if(unjamEnd < 0 || Timer.getFPGATimestamp() > unjamEnd) {
            setAgitatorPowerRaw(intakePowerProperty.get());
        }
        isIntaking = true;
    }

    public void stop() {
        setAgitatorPower(0);
    }

    @Override
    public void updatePeriodicData() {
        agitatorMotor.updateTelemetryProperties();
        if(agitatorMotor.getOutputCurrent() > agitatorOverCurrentThreshold.get()) {
            if(isTrackingOverCurrent) {
                if(Timer.getFPGATimestamp() - overCurrentStart > agitatorStallDuration.get()) {
                    unjamEnd = Timer.getFPGATimestamp() + unjamDuration.get();
                }
            }
            else {
                isTrackingOverCurrent = true;
                overCurrentStart = Timer.getFPGATimestamp();
            }
        }
        else {
            isTrackingOverCurrent = false;
        }
        
        if(unjamEnd > 0 && Timer.getFPGATimestamp() < unjamEnd) {
            if(isIntaking) {
                setAgitatorPowerRaw(unjamPower.get());
            }
        }
        else {
            unjamEnd = -1;
        }
    }
    
}
