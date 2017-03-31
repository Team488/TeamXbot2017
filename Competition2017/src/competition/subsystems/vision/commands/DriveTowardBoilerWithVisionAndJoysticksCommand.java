package competition.subsystems.vision.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.vision.DetectedBoiler;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.XJoystick;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.XPropertyManager;

public class DriveTowardBoilerWithVisionAndJoysticksCommand extends BaseCommand {

    private final VisionSubsystem visionSubsystem;
    private final DriveSubsystem driveSubsystem;
    
    private final PIDManager rotationPid;
    private XJoystick controlJoystick;
    

    @Inject
    public DriveTowardBoilerWithVisionAndJoysticksCommand(
            VisionSubsystem visionSubsystem,
            DriveSubsystem driveSubsystem,
            PIDFactory pidFactory,
            XPropertyManager propMan)
    {
        this.visionSubsystem = visionSubsystem;
        this.driveSubsystem = driveSubsystem;
        
        rotationPid = pidFactory.createPIDManager("Robot vision rotation while driving", 0.9, 0, 0, 0, 1, -1, 0.002, 0.002, 0);
        
        this.requires(this.driveSubsystem);
    }
    
    public void setControlJoystick(XJoystick controlJoystick) {
        this.controlJoystick = controlJoystick;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        
        if(controlJoystick == null) {
            log.warn("No joystick specified for joystick-based drive command!");
        }
    }

    @Override
    public void execute() {
        DetectedBoiler target = visionSubsystem.getSustainedTrackedBoiler();
        
        rotationPid.setIMask(target == null);
        double rotatePower = rotationPid.calculate(target == null ? 0 : target.offsetX, 0);
        double forwardPower = controlJoystick == null ? 0 : controlJoystick.getVector().y;
        
        driveSubsystem.tankDrivePowerMode(forwardPower + rotatePower, forwardPower - rotatePower);
    }
    
    public void end(){
        driveSubsystem.tankDrivePowerMode(0, 0);
    }
}
