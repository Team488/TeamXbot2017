package competition.subsystems.autonomous;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class BreakBaselineCommandGroup extends CommandGroup {

    @Inject
    public BreakBaselineCommandGroup(
            DriveForDistanceCommand driveForDistance, 
            PoseSubsystem poseSubsystem,
            XPropertyManager propMan) {
        
        driveForDistance.setDeltaDistance(poseSubsystem.getDistanceFromWallToBaseline());
        
        this.addParallel(driveForDistance);
    }
}
