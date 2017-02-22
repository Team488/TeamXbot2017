package competition.subsystems.shooter_belt;

import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;

import competition.subsystems.BaseXCANTalonSpeedControlledSubsystem;
import competition.subsystems.RobotSide;
import javafx.geometry.Side;
import telemetry.InfluxDBConnection;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShooterBeltSubsystem extends BaseXCANTalonSpeedControlledSubsystem {

    private final RobotSide side;
    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;
    protected final DoubleProperty beltIntakeTargetSpeed;
    private final InfluxDBConnection influxConnection;
    
    public ShooterBeltSubsystem(
            RobotSide side,
            int masterChannel,
            boolean invertMaster,
            boolean invertMasterSensor,
            WPIFactory factory, 
            PIDPropertyManager pidPropertyManager,
            XPropertyManager propManager,
            InfluxDBConnection influxConnection){
        super(
                side + " ShooterBelt",
                masterChannel,
                invertMaster,
                invertMasterSensor,
                factory,
                pidPropertyManager,
                propManager);
        this.side = side;
        this.influxConnection = influxConnection;
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
        
        Point masterPoint = Point.measurement(this.getClass().getSimpleName())
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("side", side.toString().toLowerCase())
                .addField("power", masterMotor.get())
                .addField("current", masterMotor.getOutputCurrent())
                .build();
        influxConnection.writePoint(masterPoint);
    }
}