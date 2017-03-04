package competition.subsystems.vision.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.vision.DetectedBoiler;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.XPropertyManager;

public class RotateRobotToBoilerCommand extends BaseCommand {

    final VisionSubsystem visionSubsystem;
    final DriveSubsystem driveSubsystem;
    
    final PIDManager rotationPid;

    @Inject
    public RotateRobotToBoilerCommand(
            VisionSubsystem visionSubsystem,
            DriveSubsystem driveSubsystem,
            PIDFactory pidFactory,
            XPropertyManager propMan)
    {
        this.visionSubsystem = visionSubsystem;
        this.driveSubsystem = driveSubsystem;
        
        rotationPid = pidFactory.createPIDManager("Robot vision rotation", 0.6, 0, 0, 0, 1, -1, 0.002, 0.002, 0);
        
        this.requires(this.driveSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        DetectedBoiler target = visionSubsystem.getTrackedBoiler();
        
        rotationPid.setIMask(target == null);
        double power = rotationPid.calculate(0, target == null ? 0 : target.offsetX * 0.6);
        
        driveSubsystem.tankDrivePowerMode(power, -power);
    }
    
    public boolean isFinished(){
        return visionSubsystem.getTrackedBoiler() != null && rotationPid.isOnTarget();
    }
    
    public void end(){
        driveSubsystem.tankDrivePowerMode(0, 0);
    }

}
