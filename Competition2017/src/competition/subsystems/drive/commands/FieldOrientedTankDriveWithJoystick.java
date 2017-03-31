package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.controls.sensors.XJoystick;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class FieldOrientedTankDriveWithJoystick extends BaseDriveCommand {

    private final PoseSubsystem poseSubsystem;
    
    private XJoystick joystick;
    
    private final PIDManager headingDrivePid;
    public final double defaultPValue = 1 / 80d;
    
    protected final DoubleProperty joystickMagnitudeDeadband;
    
    @Inject
    public FieldOrientedTankDriveWithJoystick(
            OperatorInterface oi, 
            DriveSubsystem driveSubsystem, 
            PoseSubsystem poseSubsystem,
            PIDFactory pidFactory,
            XPropertyManager propMan) {
        super(driveSubsystem);
        this.poseSubsystem = poseSubsystem;
        this.joystick = oi.leftJoystick;
        headingDrivePid = pidFactory.createPIDManager("Field-Oriented rotate to joystick vector", defaultPValue, 0, 0, 0, 1, -1, 3, 3.0 / 20, 1);
        joystickMagnitudeDeadband = propMan.createPersistentProperty("Field-Oriented Joystick magnitude threshold", 0.2);
    }

    /**
     * A setter method that selects which ever desired joystick the operator wants
     * @param joystick the joystick that will control the command
     */
    public void setJoystick(XJoystick joystick) {
        this.joystick = joystick;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double translationPower = joystick.getVector().getMagnitude();
        
        if (translationPower > joystickMagnitudeDeadband.get()) {
            double headingError =  poseSubsystem.getCurrentHeading().difference(joystick.getVector().getAngle());
            double rotationPower = -headingDrivePid.calculate(0, headingError);
            driveSubsystem.tankDrivePowerMode(translationPower - rotationPower, translationPower + rotationPower);
        } else {
            driveSubsystem.tankDrivePowerMode(0, 0);
        }
    }
}
