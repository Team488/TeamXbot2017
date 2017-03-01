package competition.subsystems.drive;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.shift.ShiftSubsystem;

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
    public final XCANTalon leftDriveFollower;
    public final XCANTalon rightDrive;
    public final XCANTalon rightDriveFollower;
    
    private final DoubleProperty encoderCodesProperty;
    private final DoubleProperty maxSpeedProperty;
    private final DoubleProperty pVelProp;
    private final DoubleProperty iVelprop;
    private final DoubleProperty dVelProp;
    private final DoubleProperty fVelProp;
    private final DoubleProperty leftDriveEncoderTicksProp;
    private final DoubleProperty rightDriveEncoderTicksProp;
    private final DoubleProperty ticksPerInch;
    
    private double previousLeftTicks;
    private double previousRightTicks;
    private double leftInchesTraveled;
    private double rightInchesTraveled;
    
    
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
        
        ticksPerInch = propManager.createPersistentProperty("Ticks per inch", 25.33);
        
        this.leftDrive = factory.getCANTalonSpeedController(34);
        this.leftDrive.setInverted(true);
        this.leftDriveFollower = factory.getCANTalonSpeedController(35);
        configMotorTeam(leftDrive, leftDriveFollower);
        leftDrive.createTelemetryProperties("Left primary", propManager);
        leftDriveFollower.createTelemetryProperties("Left follower", propManager);
        
        this.rightDrive = factory.getCANTalonSpeedController(21);
        this.rightDrive.reverseSensor(true);
        this.rightDriveFollower = factory.getCANTalonSpeedController(20);
        configMotorTeam(rightDrive, rightDriveFollower);
        rightDrive.createTelemetryProperties("Right primary", propManager);
        rightDriveFollower.createTelemetryProperties("Right follower", propManager);
        
        previousLeftTicks = leftDrive.getPosition();
        previousRightTicks = rightDrive.getPosition();
        leftInchesTraveled = 0;
        rightInchesTraveled = 0;
    }

    private void configMotorTeam(XCANTalon master, XCANTalon slave) {
        // TODO: Check faults and voltage/temp/current
      
        // Master config
        master.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        master.setBrakeEnableDuringNeutral(false);
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
        return ticksPerInch.get();
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
        leftDriveFollower.updateTelemetryProperties();
        rightDrive.updateTelemetryProperties();
        rightDriveFollower.updateTelemetryProperties();
        
        leftDriveEncoderTicksProp.set(leftDrive.getPosition());
        rightDriveEncoderTicksProp.set(rightDrive.getPosition());
        
        leftInchesTraveled += convertTicksToInches(leftDrive.getPosition() - previousLeftTicks);
        rightInchesTraveled += convertTicksToInches(rightDrive.getPosition() - previousRightTicks);
        previousLeftTicks = leftDrive.getPosition();
        previousRightTicks = rightDrive.getPosition();
        
    }
}