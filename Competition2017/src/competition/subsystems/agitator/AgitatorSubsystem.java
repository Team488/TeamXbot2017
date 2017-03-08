package competition.subsystems.agitator;

import com.ctre.CANTalon.TalonControlMode;

import competition.subsystems.RobotSide;

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

        this.agitatorMotor = factory.getCANTalonSpeedController(motor);

        agitatorMotor.setBrakeEnableDuringNeutral(false);
        agitatorMotor.setProfile(0);
        agitatorMotor.setInverted(invertMotor);
        agitatorMotor.setControlMode(TalonControlMode.PercentVbus);
        
        this.agitatorMotor.createTelemetryProperties(side.toString(), propManager);
    }

    public void setAgitatorPower(double power) {
        agitatorMotor.ensureTalonControlMode(TalonControlMode.PercentVbus);
        agitatorMotor.set(power);
    }
  
    public RobotSide getSide() {
        return side;
    }

    public void eject() {
        agitatorMotor.set(ejectPowerProperty.get());
    }

    public void intake() {
        agitatorMotor.set(intakePowerProperty.get());
    }

    public void stop() {
        agitatorMotor.set(0);
    }

    @Override
    public void updatePeriodicData() {
        agitatorMotor.updateTelemetryProperties();
    }
    
}
