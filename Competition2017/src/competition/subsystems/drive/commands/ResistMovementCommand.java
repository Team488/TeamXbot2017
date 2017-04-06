package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.XPropertyManager;

public class ResistMovementCommand extends BaseDriveCommand {
    private PIDManager leftPid;
    private PIDManager rightPid;

    private double targetLeftPosition;
    private double targetRightPosition;
    
    @Inject
    public ResistMovementCommand(
            DriveSubsystem driveSubsystem,
            PIDFactory pidFactory,
            XPropertyManager propMan) {
        super(driveSubsystem);
        
        final String sharedPidKey = "Resist movement";
        leftPid = pidFactory.createPIDManager(sharedPidKey, 1/12d, 0.01, 0, 0, 1, -1);
        rightPid = pidFactory.createPIDManager(sharedPidKey, 1/12d, 0.01, 0, 0, 1, -1);
    }
    
    @Override
    public void initialize() {
        leftPid.reset();
        rightPid.reset();

        targetLeftPosition = driveSubsystem.getLeftDistanceInInches();
        targetRightPosition = driveSubsystem.getRightDistanceInInches();
    }

    @Override
    public void execute() {
        double leftPower = leftPid.calculate(targetLeftPosition, driveSubsystem.getLeftDistanceInInches());
        double rightPower = rightPid.calculate(targetRightPosition, driveSubsystem.getRightDistanceInInches());
        driveSubsystem.tankDrivePowerMode(leftPower, rightPower);
    }

}
