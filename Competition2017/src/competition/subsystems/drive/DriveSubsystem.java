package competition.subsystems.drive;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class DriveSubsystem extends BaseSubsystem implements PeriodicDataSource {

    public final XCANTalon leftDrive;
    public final XCANTalon leftDriveSlave;
    public final XCANTalon rightDrive;
    public final XCANTalon rightDriveSlave;
    
    private final DoubleProperty encoderCodesProperty;
    private final DoubleProperty maxSpeedProperty;
    private final DoubleProperty pVelProp;
    private final DoubleProperty iVelprop;
    private final DoubleProperty dVelProp;
    private final DoubleProperty fVelProp;
    private final DoubleProperty leftDriveEncoderTicksProp;
    private final DoubleProperty rightDriveEncoderTicksProp;
    private final DoubleProperty startPositionTicks;
    private final DoubleProperty ticksPerInchHighGear;
    private final DoubleProperty ticksPerInchLowGear;
    
    private final ShiftSubsystem shiftSubsystem;
    
    private double leftInchesTraveled;
    private double rightInchesTraveled;
    private double previousLeftTicks;
    private double previousRightTicks;
    
    @Inject
    public DriveSubsystem(WPIFactory factory, XPropertyManager propManager, ShiftSubsystem shift) {
        log.info("Creating");

        // TODO: Update these defaults. The current values are blind guesses.
        encoderCodesProperty = propManager.createPersistentProperty("Drive encoder codes per rev", 512);
        maxSpeedProperty = propManager.createPersistentProperty("Max drive motor speed (rotations per second)", 5);

        pVelProp = propManager.createPersistentProperty("Drive vel P", 2);
        iVelprop = propManager.createPersistentProperty("Drive vel I", 0);
        dVelProp = propManager.createPersistentProperty("Drive vel D", -100);
        fVelProp = propManager.createPersistentProperty("Drive vel F", 0);
        
        leftDriveEncoderTicksProp = propManager.createEphemeralProperty("Left drive encoder ticks", 0);
        rightDriveEncoderTicksProp = propManager.createEphemeralProperty("Right drive encoder ticks", 0);
                
        startPositionTicks = propManager.createEphemeralProperty("Start position ticks", 0);
        ticksPerInchHighGear = propManager.createEphemeralProperty("Ticks Per Inch High Gear", 0);
        ticksPerInchLowGear = propManager.createEphemeralProperty("Ticks Per Inch Low Gear", 25.33);
        
        this.leftDrive = factory.getCANTalonSpeedController(34);
        leftDrive.setInverted(true);
        this.leftDriveSlave = factory.getCANTalonSpeedController(35);
        configMotorTeam(leftDrive, leftDriveSlave);
        leftDrive.createTelemetryProperties("Left master", propManager);
        leftDriveSlave.createTelemetryProperties("Left slave", propManager);
        
        this.rightDrive = factory.getCANTalonSpeedController(21);
        this.rightDriveSlave = factory.getCANTalonSpeedController(20);
        configMotorTeam(rightDrive, rightDriveSlave);
        rightDrive.createTelemetryProperties("Right master", propManager);
        rightDriveSlave.createTelemetryProperties("Right slave", propManager);
        
        this.shiftSubsystem = shift;
        
        previousLeftTicks = startPositionTicks.get();
        previousRightTicks = startPositionTicks.get();
        leftInchesTraveled = 0;
        rightInchesTraveled = 0;
    }

    private void configMotorTeam(XCANTalon master, XCANTalon slave) {
        // TODO: Check faults and voltage/temp/current
      
        // Master config
        master.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        master.setBrakeEnableDuringNeutral(false);
        master.reverseSensor(true);
        master.enableLimitSwitches(false, false);
        
        master.configNominalOutputVoltage(0,  -0);
        master.configPeakOutputVoltage(12, -12);

        master.setProfile(0);
        master.setF(0);
        updateMotorConfig(master);
        master.setControlMode(TalonControlMode.Speed);
        master.set(0);
        
        // Slave config
        slave.configNominalOutputVoltage(0,  -0);
        slave.configPeakOutputVoltage(12, -12);
        slave.enableLimitSwitches(false, false);
        slave.setControlMode(TalonControlMode.Follower);
        slave.set(master.getDeviceID());
    }
    
    /**
     * Converts joystick input into a velocity goal
     * @param power the joystick value that is being passed through
     * @return the velocity goal
     */
    public double convertPowerToVelocityTarget(double power) {
        double maxTicksPerTenMs = maxSpeedProperty.get() * encoderCodesProperty.get() / 100;
        return power * maxTicksPerTenMs;
    }
    
    public double convertInchesPerSecToVelocityTarget(double inchesPerSec) {
        double maxTicksPerTenMs = maxSpeedProperty.get() * encoderCodesProperty.get() / 100;
        return power * maxTicksPerTenMs;
    }
    
    private void ensureModeForTalon(XCANTalon talon, TalonControlMode mode) {
        if (talon.getControlMode() != mode) {
            talon.setControlMode(mode);
        }
    }
    
    private void ensureSpeedModeForDrive() {
        ensureModeForTalon(leftDrive, TalonControlMode.Speed);
        ensureModeForTalon(rightDrive, TalonControlMode.Speed);
    }
    
    private void ensurePowerModeForDrive() {
        ensureModeForTalon(leftDrive, TalonControlMode.PercentVbus);
        ensureModeForTalon(rightDrive, TalonControlMode.PercentVbus);
    }

    public void tankDriveVelocityPid(double leftPower, double rightPower) {
        // TODO: Move parameter updates to something more consistent
        ensureSpeedModeForDrive();
        
        // Coerce powers into appropriate limits
        leftPower = MathUtils.constrainDoubleToRobotScale(leftPower);
        rightPower = MathUtils.constrainDoubleToRobotScale(rightPower);
        
        updateMotorConfig(leftDrive);
        updateMotorConfig(rightDrive);

        leftDrive.set(convertPowerToVelocityTarget(leftPower));
        rightDrive.set(convertPowerToVelocityTarget(rightPower));

        updatePeriodicData();
    }
    
    public void tankDriveVelocityPid(double leftInchesPerSec, double rightInchesPerSec) {
        // TODO: Move parameter updates to something more consistent
        ensureSpeedModeForDrive();
        
        updateMotorConfig(leftDrive);
        updateMotorConfig(rightDrive);

        leftDrive.set(convertPowerToVelocityTarget(leftPower));
        rightDrive.set(convertPowerToVelocityTarget(rightPower));

        updatePeriodicData();
    }
    
    /**
     * Simple power based tank drive method of the robot
     */
    public void tankDrivePowerMode(double leftPower, double rightPower) {
        ensurePowerModeForDrive();
        
        // Coerce powers into appropriate limits
        leftPower = MathUtils.constrainDoubleToRobotScale(leftPower);
        rightPower = MathUtils.constrainDoubleToRobotScale(rightPower);
       
        leftDrive.set(leftPower);
        rightDrive.set(rightPower);
        
        updatePeriodicData();
    }
    
    /**
     * Updates the motor with new values without resetting the robot
     * @param motor used to access the motor to set and give data
     */
    private void updateMotorConfig(XCANTalon motor) {
        motor.setP(pVelProp.get());
        motor.setI(iVelprop.get());
        motor.setD(dVelProp.get());
        motor.setF(fVelProp.get());
    }
    
    private double getTicksPerInch() {
        return shiftSubsystem.getGear() == Gear.HIGH_GEAR ? ticksPerInchHighGear.get() : ticksPerInchLowGear.get();
    }
    
    public double convertTicksToInches(double ticks) {
        return ticks / getTicksPerInch();
    }
    
    public double convertInchesToTicks(double inches) {
        return inches * getTicksPerInch();
    }
    
    /**
     * Note - should normally only be called by the PoseSubsystem!
     */
    public double getLeftDistanceInInches() {
        return leftInchesTraveled;
    }
    
    /**
     * Note - should normally only be called by the PoseSubsystem!
     */
    public double getRightDistanceInInches() {
        return rightInchesTraveled;
    }

    /**
     * Get values from robot to output on the teleop interface
     * IMPORTANT: When setting power to motors call this method
     */
    @Override
    public void updatePeriodicData() {
        leftDrive.updateTelemetryProperties();
        leftDriveSlave.updateTelemetryProperties();
        rightDrive.updateTelemetryProperties();
        rightDriveSlave.updateTelemetryProperties();
        
        leftDriveEncoderTicksProp.set(leftDrive.getPosition());
        rightDriveEncoderTicksProp.set(rightDrive.getPosition());
        
        leftInchesTraveled += convertTicksToInches(leftDrive.getPosition() - previousLeftTicks);
        rightInchesTraveled += convertTicksToInches(rightDrive.getPosition() - previousRightTicks);
        previousLeftTicks = leftDrive.getPosition();
        previousRightTicks = rightDrive.getPosition();
        
    }
}