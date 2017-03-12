package competition.subsystems.autonomous.selection;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class SetupBreakBaselineCommand extends BaseAutonomousCommandSetter {

    public final DriveForDistanceCommand breakBaselineAuto;
    // distance to break baseline from a position with both back wheels on the wall
    private DoubleProperty distanceFromWallToBaseline;

    public SetupBreakBaselineCommand(AutonomousCommandSelector autonomousCommandSelector,
            DriveForDistanceCommand driveForDistance, XPropertyManager propMan) {
        super(autonomousCommandSelector);
        distanceFromWallToBaseline = propMan.createPersistentProperty("distance from wall to break baseline in inches", 96);
        this.breakBaselineAuto = driveForDistance;
        breakBaselineAuto.setDeltaDistance(distanceFromWallToBaseline);
    }

    @Override
    public void initialize() {
        this.autonomousCommandSelector.setCurrentAutonomousCommand(breakBaselineAuto);
    }

}
