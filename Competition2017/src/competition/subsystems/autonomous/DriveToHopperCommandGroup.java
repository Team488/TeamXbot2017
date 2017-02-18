package competition.subsystems.autonomous;

import com.google.inject.Provider;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.command.TimeoutCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveToHopperCommandGroup extends CommandGroup{
    private DoubleProperty waitTimeForFuelCollection;
    private DoubleProperty distanceFromWallToBaseline;
    private DoubleProperty distanceFromTurningPointToHopper;
    private DoubleProperty headingToFaceHopper;
    
    public DriveToHopperCommandGroup(XPropertyManager propManager,
            Provider<DriveForDistanceCommand> driveForDistanceProvider,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider) {
        waitTimeForFuelCollection = propManager.createPersistentProperty("Wait Time For Fuel Collection In Seconds", 5);
        distanceFromWallToBaseline = propManager.createPersistentProperty("Distance From Wall To Baseline In Inches", 37.25);
        distanceFromTurningPointToHopper = propManager
                .createPersistentProperty("Distance From Turning Point On Baseline To Hopper In Inches", 43.25);
        headingToFaceHopper = propManager.createPersistentProperty("Heading To Face Hopper In Degrees", 90);
        
        // start with the back wheels both on the wall with the left wheel on the very edge of the wall and move to
        // turning point on baseline (does not move past baseline)
        DriveForDistanceCommand moveToTurningPointCommand = driveForDistanceProvider.get();
        moveToTurningPointCommand.setDeltaDistance(distanceFromWallToBaseline);
        this.addSequential(moveToTurningPointCommand);

        // change direction to face hopper
        RotateToHeadingCommand faceHopperCommand = rotateToHeadingProvider.get();
        faceHopperCommand.setTargetHeadingProp(headingToFaceHopper);
        this.addSequential(faceHopperCommand);

        // move from the turning point on baseline to hopper
        DriveForDistanceCommand moveToHopperCommand = driveForDistanceProvider.get();
        moveToHopperCommand.setDeltaDistance(distanceFromTurningPointToHopper.get());
        this.addSequential(moveToHopperCommand);

        // wait at the hopper to collect fuel
        TimeoutCommand waitForFuelCollectionCommand = new TimeoutCommand(waitTimeForFuelCollection);
        this.addSequential(waitForFuelCollectionCommand);
        
    }
}