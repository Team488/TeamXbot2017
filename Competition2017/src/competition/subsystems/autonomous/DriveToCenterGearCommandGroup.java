package competition.subsystems.autonomous;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.DriveInfinitelyCommand;
import competition.subsystems.drive.commands.TogglePrecisionMode;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.command.BaseCommandGroup;

public class DriveToCenterGearCommandGroup extends BaseCommandGroup {

    @Inject
    public DriveToCenterGearCommandGroup(
            DriveForDistanceCommand driveForDistance, 
            PoseSubsystem poseSubsystem,
            Provider<TogglePrecisionMode> toggleProvider){
        
        this.addSequential(toggleProvider.get());
        
        driveForDistance.setDeltaDistance(poseSubsystem.getDistanceFromWallToCenterGear());
        this.addSequential(driveForDistance, poseSubsystem.getCenterGearAutoMaxTime());
        
        
        this.addSequential(toggleProvider.get());
    }
}
