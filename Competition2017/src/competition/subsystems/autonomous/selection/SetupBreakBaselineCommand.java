package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.properties.XPropertyManager;

public class SetupBreakBaselineCommand extends BaseAutonomousCommandSetter {

    public final DriveForDistanceCommand breakBaselineAuto;
    private PoseSubsystem poseSubsystem;
    
    @Inject
    public SetupBreakBaselineCommand(
            AutonomousCommandSelector autonomousCommandSelector,
            DriveForDistanceCommand driveForDistance,
            PoseSubsystem poseSubsystem,
            XPropertyManager propMan) {
        super(autonomousCommandSelector);
        this.breakBaselineAuto = driveForDistance;
        this.poseSubsystem = poseSubsystem;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        breakBaselineAuto.setDeltaDistance(poseSubsystem.getDistanceFromWallToBaseline());
        this.autonomousCommandSelector.setCurrentAutonomousCommand(breakBaselineAuto);
    }

}
