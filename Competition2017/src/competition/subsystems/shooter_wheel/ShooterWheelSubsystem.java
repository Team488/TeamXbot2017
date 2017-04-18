package competition.subsystems.shooter_wheel;

import competition.subsystems.BaseXCANTalonPairSpeedControlledSubsystem;
import competition.subsystems.RobotSide;
import xbot.common.controls.actuators.XServo;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShooterWheelSubsystem extends BaseXCANTalonPairSpeedControlledSubsystem {
    
    private final RobotSide side;
    protected final DoubleProperty flushToBoilerTargetSpeed;
    protected final DoubleProperty trimFlushToBoilerSpeed;
    
    protected final DoubleProperty offsetFromHopperTargetSpeed;
    
    protected final DoubleProperty wheelSpeedThresholdPercentage;
    protected final BooleanProperty isShooterAtSpeed;

    protected final XServo aimServo;
    protected final boolean aimServoInverted;

    protected final DoubleProperty flushToBoilerServoPosition;
    protected final DoubleProperty offsetFromHopperServoPosition;
    
    public enum TypicalShootingPosition {
        FlushToBoiler,
        OffsetFromHopper
    }
   
    public ShooterWheelSubsystem(
            int masterChannel,
            int followerChannel,
            int aimServoChannel,
            boolean masterInverted,
            boolean masterSensorInverted,
            boolean followerInverted,
            boolean aimServoInverted,
            RobotSide side,
            PIDPropertyManager pidPropertyManager,
            WPIFactory factory,
            XPropertyManager propManager) {
        super(
                side + "ShooterWheel",
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
        this.aimServoInverted = aimServoInverted;
        
        aimServo = factory.getServo(aimServoChannel);
        
        flushToBoilerTargetSpeed = 
                propManager.createPersistentProperty(side + " flush to boiler target speed", 9_000);
        trimFlushToBoilerSpeed =
                propManager.createEphemeralProperty(side + " trim speed", 0.0);
        flushToBoilerServoPosition = 
                propManager.createPersistentProperty(side + " flush to boiler servo position", 0);
        
        offsetFromHopperTargetSpeed = 
                propManager.createPersistentProperty(side + " offset from hopper target speed", 11_000);
        offsetFromHopperServoPosition = 
                propManager.createPersistentProperty(side + " offset from hopper servo position", 1);
        
        wheelSpeedThresholdPercentage = 
                propManager.createPersistentProperty("Wheel speed threshold percentage for feeding", 0.75);
        
        isShooterAtSpeed = 
                propManager.createPersistentProperty("Is " + side + "Shooter wheel at speed?", false);
    }
    
    public RobotSide getSide() {
        return side;
    }
    
    public void trimRangePower(TypicalShootingPosition range, double trimAmount) {
        switch (range) {
            case FlushToBoiler:
                trimFlushToBoilerSpeed.set(trimFlushToBoilerSpeed.get() + trimAmount);
                
                if (trimFlushToBoilerSpeed.get() > 10_000) {
                    trimFlushToBoilerSpeed.set(10_000);
                }
                
                if (trimFlushToBoilerSpeed.get() < -10_000) {
                    trimFlushToBoilerSpeed.set(-10_000);
                }
                break;
            default: 
                // nothing to do here
        }
    }
    
    public double translateTypicalShootingPositionToDistance(TypicalShootingPosition range) {
        switch (range) {
            case FlushToBoiler:
                return 0;
            default:
                return 0;
        }
    }
    
    public double getTargetSpeedForRange(TypicalShootingPosition range) {
        switch (range) {
            case FlushToBoiler:
                return flushToBoilerTargetSpeed.get() + trimFlushToBoilerSpeed.get();
            case OffsetFromHopper:
                return offsetFromHopperTargetSpeed.get();
            default:
                return 0;
        }
    }
    
    protected double getAimServoPositionForRange(TypicalShootingPosition range) {
        switch (range) {
            case OffsetFromHopper:
                return offsetFromHopperServoPosition.get();
            default:
                return flushToBoilerServoPosition.get();
        }
    }
    
    public void runForRange(TypicalShootingPosition range) {
        final double normalizedServoPosition = getAimServoPositionForRange(range);
        aimServo.set(aimServoInverted ? (1 - normalizedServoPosition) : normalizedServoPosition);
        setTargetSpeed(getTargetSpeedForRange(range));
    }
    
    public boolean isWheelAtSpeed() {
        // Speed is in native units and can be negative
        double currentSpeed = Math.abs(super.getSpeed());
        if (currentSpeed <= 1) {
            return false;
        }
        return currentSpeed >= super.getTargetSpeed() * wheelSpeedThresholdPercentage.get();
    }
    
    @Override
    public void updatePeriodicData() {
        super.updatePeriodicData();
        isShooterAtSpeed.set(isWheelAtSpeed());
    }
}

