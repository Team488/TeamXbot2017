package competition.subsystems.shooter_wheel;

import competition.subsystems.BaseXCANTalonPairSpeedControlledSubsystem;
import competition.subsystems.RobotSide;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShooterWheelSubsystem extends BaseXCANTalonPairSpeedControlledSubsystem {
    
    private final RobotSide side;
    protected final DoubleProperty flushToBoilerTargetSpeed;
    protected final DoubleProperty trimFlushToBoilerSpeed;
    protected final DoubleProperty wheelSpeedThresholdPercentage;
    protected final BooleanProperty isShooterAtSpeed;
    
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
        flushToBoilerTargetSpeed = 
                propManager.createPersistentProperty(side + " flush to boiler target speed", 9000);
        trimFlushToBoilerSpeed =
                propManager.createEphemeralProperty(side + " trim speed", 0.0);
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
                
                if (trimFlushToBoilerSpeed.get() > 10000) {
                    trimFlushToBoilerSpeed.set(10000);
                }
                
                if (trimFlushToBoilerSpeed.get() < -10000) {
                    trimFlushToBoilerSpeed.set(-10000);
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
    
    public double getTargetSpeedForRange(double rangeInInches) {
        // TODO: convert from rangeInInches to target speeds (pending task)
        // For now everything uses the same flushToBoilerTargetSpeed regardless
        
        return flushToBoilerTargetSpeed.get() + trimFlushToBoilerSpeed.get();
    }
    
    /**
     * Set the ShooterWheel for any robot range.
     * @param rangeInInches Inches between the front bumper and the boiler wall
     */
    public void setTargetSpeedForRange(double rangeInInches) {
        // some day we may have an actual formula here, interpolating between known points.
        // (flush to boiler, one robot width, some other range...)
        // For now, it's just this one range.
        setTargetSpeed(getTargetSpeedForRange(rangeInInches));
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

