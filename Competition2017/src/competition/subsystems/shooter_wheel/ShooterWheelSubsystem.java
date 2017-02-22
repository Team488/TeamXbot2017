package competition.subsystems.shooter_wheel;

import org.influxdb.dto.Point;

import competition.subsystems.BaseXCANTalonPairSpeedControlledSubsystem;
import competition.subsystems.RobotSide;

import telemetry.InfluxDBConnection;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShooterWheelSubsystem extends BaseXCANTalonPairSpeedControlledSubsystem {
    
    private final RobotSide side;
    protected final DoubleProperty flushToBoilerTargetSpeed;
    private final InfluxDBConnection influxConnection;
    
    public enum TypicalShootingPosition {
        FlushToBoiler
    }
   
    public ShooterWheelSubsystem(
            int masterChannel,
            int followerChannel,
            boolean masterInverted,
            boolean masterSensorInverted,
            boolean followerInverted,
            RobotSide side,
            PIDPropertyManager pidPropertyManager,
            WPIFactory factory,
            XPropertyManager propManager,
            InfluxDBConnection influxConnection) {
        super(
                side+" ShooterWheel",
                masterChannel,
                followerChannel,
                masterInverted,
                masterSensorInverted,
                followerInverted,
                factory,
                pidPropertyManager,
                propManager);
        log.info("Creating");
        this.side = side;
        flushToBoilerTargetSpeed = 
                propManager.createPersistentProperty(side + " flush to boiler target speed", 3500);
        this.influxConnection = influxConnection;
    }
    
    public RobotSide getSide(){
        return side;
    }
    
    public double translateTypicalShootingPositionToDistance(TypicalShootingPosition range) {
        switch (range) {
            case FlushToBoiler:
                return 0;
            default:
                return 0;
        }
    }
    
    /**
     * Set the ShooterWheel for any robot range.
     * @param rangeInInches Inches between the front bumper and the boiler wall
     */
    public void setTargetSpeedForRange(double rangeInInches) {
        // some day we may have an actual formula here, interpolating between known points.
        // (flush to boiler, one robot width, some other range...)
        // For now, it's just this one range.
        setTargetSpeed(flushToBoilerTargetSpeed.get());
    }
    
    @Override
    public void updatePeriodicData() {
        super.updatePeriodicData();
        
        Point primaryPoint = masterMotor.getTelemetryPoint(this.getClass().getSimpleName(), side.toString().toLowerCase(), false);
        influxConnection.writePoint(primaryPoint);
    }
}

