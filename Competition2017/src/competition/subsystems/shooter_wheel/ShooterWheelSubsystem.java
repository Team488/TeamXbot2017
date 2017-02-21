package competition.subsystems.shooter_wheel;

import competition.InfluxDBWriter;
import competition.subsystems.BaseXCANTalonPairSpeedControlledSubsystem;
import competition.subsystems.RobotSide;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShooterWheelSubsystem extends BaseXCANTalonPairSpeedControlledSubsystem {
    
    private final RobotSide side;
    
    protected final DoubleProperty flushToBoilerTargetSpeed;
    
    private InfluxDBWriter influxWriter;
    
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
            InfluxDBWriter influxWriter) {
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
        this.influxWriter = influxWriter;
        this.influxWriter.setMeasurementName("ShooterWheelSubsystem");
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
        if (side == RobotSide.Left) {
            this.influxWriter.writeData("leftSideShooterWheelPower", masterMotor.get());
            this.influxWriter.writeData("leftSideShooteWheelCurrent", masterMotor.getOutputCurrent());
            this.influxWriter.writeData("leftSideShooterWheelFollowerPower", followerMotor.get());
            this.influxWriter.writeData("leftSideShooteWheelFollowerCurrent", followerMotor.getOutputCurrent());
        } else if (side == RobotSide.Right) {
            this.influxWriter.writeData("rightSideShooterWheelPower", masterMotor.get());
            this.influxWriter.writeData("rightSideShooteWheelCurrent", masterMotor.getOutputCurrent());
            this.influxWriter.writeData("rightSideShooterWheelFollowerPower", followerMotor.get());
            this.influxWriter.writeData("rightSideShooteWheelFollowerCurrent", followerMotor.getOutputCurrent());
        }
    }
}

