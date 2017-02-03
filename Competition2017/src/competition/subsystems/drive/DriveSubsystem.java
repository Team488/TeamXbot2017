package competition.subsystems.drive;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class DriveSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(DriveSubsystem.class);

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
    private final DoubleProperty ticksPerInch;
    private final DoubleProperty startPositionTicks;
    private final DoubleProperty currentDisplacementInchProp;
    
    @Inject
    public DriveSubsystem(WPIFactory factory, XPropertyManager propManager) {
        log.info("Creating DriveSubsystem");

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
        
        startPositionTicks = propManager.createEphemeralProperty("Start position ticks", 0);
        currentDisplacementInchProp = propManager.createEphemeralProperty("Current position inches", 0);
        
        this.leftDrive = factory.getCANTalonSpeedController(3);
        this.leftDriveSlave = factory.getCANTalonSpeedController(4);
        configMotorTeam(leftDrive, leftDriveSlave);
        leftDrive.createTelemetryProperties("Left master", propManager);
        leftDriveSlave.createTelemetryProperties("Left slave", propManager);
        
        this.rightDrive = factory.getCANTalonSpeedController(1);
        this.rightDrive.setInverted(true);
        this.rightDriveSlave = factory.getCANTalonSpeedController(2);
        configMotorTeam(rightDrive, rightDriveSlave);
        rightDrive.createTelemetryProperties("Right master", propManager);
        rightDriveSlave.createTelemetryProperties("Right slave", propManager);

        resetDistance();
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
        
        updateMotorTelemetry();
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
        
        updateMotorTelemetry();
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
    
    /**
     * Get values from robot to output on the teleop interface
     * IMPORTANT: When setting power to motors call this method
     */
    public void updateMotorTelemetry() {
        leftDrive.updateTelemetryProperties();
        leftDriveSlave.updateTelemetryProperties();
        rightDrive.updateTelemetryProperties();
        rightDriveSlave.updateTelemetryProperties();
        
        leftDriveEncoderTicksProp.set(leftDrive.getPosition());
        rightDriveEncoderTicksProp.set(rightDrive.getPosition());
        currentDisplacementInchProp.set(getDistance());
    }
    
    public double getDistance() {
        /* TODO add more values from other motors for accuracy */
        return convertTicksToInches(rightDrive.getPosition() - startPositionTicks.get());
    }
    
    public void resetDistance() {
        startPositionTicks.set(rightDrive.getPosition());
    }
    
    public double convertTicksToInches(double ticks) {
        return ticks / ticksPerInch.get();
    }
    
    public double convertInchesToTicks(double inches) {
        return inches * ticksPerInch.get();
    }
}