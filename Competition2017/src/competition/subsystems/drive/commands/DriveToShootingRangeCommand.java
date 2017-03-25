package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class DriveToShootingRangeCommand extends BaseDriveCommand {

    private final PoseSubsystem poseSubsystem;
    private final PIDManager driveToRangePidManager;

    @Inject
    public DriveToShootingRangeCommand(
            DriveSubsystem driveSubsystem,
            PoseSubsystem poseSubsystem,
            PIDFactory pidFactory) {
        super(driveSubsystem);
        this.poseSubsystem = poseSubsystem;
        
        driveToRangePidManager = 
                pidFactory.createPIDManager(
                        "Drive to shooting range", .05, 0, 0, 0, .75, -.75, 2, 1, 1);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double power = driveToRangePidManager.calculate(
                poseSubsystem.getIdealShootingRange(), 
                poseSubsystem.getLidarFrontDistanceAverage());
        
        // However, in this case, having a currentDistance of 10 and a goal distance of 20 means you need to reverse,
        // not drive forward (since we're measuring distance from the boiler). As a result, we need to change the
        // sign on the power value.
        
        double adjustedPower = power*-1;
        
        driveSubsystem.tankDrivePowerMode(adjustedPower, adjustedPower);
    }
    
    @Override
    public boolean isFinished() {
        return driveToRangePidManager.isOnTarget();
    }
}
