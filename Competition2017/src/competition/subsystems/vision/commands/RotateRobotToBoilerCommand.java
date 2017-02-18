package competition.subsystems.vision.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.turret_rotation.TurretRotationSubsystem;
import competition.subsystems.vision.DetectedBoiler;
import competition.subsystems.vision.DetectedLiftPeg;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.math.PIDFactory;
import xbot.common.properties.XPropertyManager;

public class RotateRobotToBoilerCommand extends BaseCommand {

    final VisionSubsystem visionSubsystem;
    final DriveSubsystem driveSubsystem;
    
    final PIDManager rotationPid;

    @Inject
    public RotateRobotToBoilerCommand(
            VisionSubsystem visionSubsystem,
            DriveSubsystem driveSubsystem,
            PIDFactory pidFactory)
    {
        this.visionSubsystem = visionSubsystem;
        this.driveSubsystem = driveSubsystem;
        
        rotationPid = pidFactory.createPIDManager("Robot vision rotation", 0.6, 0, 0);
        
        this.requires(this.driveSubsystem);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        DetectedBoiler target = visionSubsystem.getTrackedBoiler();
        
        rotationPid.setIMask(target == null);
        double power = rotationPid.calculate(0, target == null ? 0 : target.offsetX * 0.6);
        
        driveSubsystem.tankDrivePowerMode(-power, -power);
    }
    
    public boolean isFinished(){
        return rotationPid.isOnTarget();
    }
    
    public void end(){
        driveSubsystem.tankDrivePowerMode(0, 0);
    }

}
