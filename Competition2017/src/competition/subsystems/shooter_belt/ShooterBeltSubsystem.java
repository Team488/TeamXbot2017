package competition.subsystems.shooter_belt;

import competition.subsystems.BaseXCANTalonSpeedControlledSubsystem;
import competition.subsystems.RobotSide;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShooterBeltSubsystem extends BaseXCANTalonSpeedControlledSubsystem {

    private final RobotSide side;
    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;
    protected final DoubleProperty beltIntakeTargetSpeed;

    public ShooterBeltSubsystem(
            RobotSide side,
            int masterChannel,
            boolean invertMaster,
            boolean invertMasterSensor,
            WPIFactory factory, 
            PIDPropertyManager pidPropertyManager,
            XPropertyManager propManager){
        super(
                side + " ShooterBelt",
                masterChannel,
                invertMaster,
                invertMasterSensor,
                factory,
                pidPropertyManager,
                propManager);
        this.side = side;
        
        intakePowerProperty = propManager.createPersistentProperty("ShooterBelt intake power", 0.5);
        ejectPowerProperty = propManager.createPersistentProperty("ShooterBelt eject power", -0.5);
        beltIntakeTargetSpeed = propManager.createPersistentProperty("ShooterBelt intake speed", 100);
    }

    public RobotSide getSide() {
        return side;
    }
    
    public void eject() {
        setPower(ejectPowerProperty.get());
    }
    
    public void intakeUsingPower() {
        setPower(intakePowerProperty.get());
    }
    
    public void intakeUsingSpeed() {
        setTargetSpeed(beltIntakeTargetSpeed.get());
    }
    
    public void stop(){
        setPower(0);
    }
    
    @Override
    public void updatePeriodicData() {
        super.updatePeriodicData();
    }
}