package competition.subsystems.autonomous;

import com.google.inject.Provider;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.vision.commands.RotateRobotToBoilerCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class MoveToHopperCommandGroup extends CommandGroup{
    private DoubleProperty angleToParallel;
    private DoubleProperty distanceFromBoiler;
    private DoubleProperty angleToBoiler;
    private DoubleProperty distanceFromTurningPointToBoiler;

    public MoveToHopperCommandGroup(
            XPropertyManager propMan,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider,
            Provider<DriveForDistanceCommand> driveForDistanceProvider,
            Provider<RotateRobotToBoilerCommand> rotateToBoilerProvider){
        angleToParallel = propMan.createPersistentProperty("Angle to line parallel to boiler", 3);
        distanceFromBoiler = propMan.createPersistentProperty("distance from robot to boiler", 6);
        angleToBoiler = propMan.createPersistentProperty("angle to face boiler", 90);
        distanceFromTurningPointToBoiler = propMan.createPersistentProperty("distance from turning point to boiler", 10);

        RotateToHeadingCommand rotateToParallelCommand = rotateToHeadingProvider.get();
        rotateToParallelCommand.setTargetHeadingProp(angleToParallel);
        this.addSequential(rotateToParallelCommand);

        DriveForDistanceCommand driveAlongParallel = driveForDistanceProvider.get();
        driveAlongParallel.setDeltaDistance(distanceFromBoiler.get() * Math.cos(angleToParallel.get()));
        this.addSequential(driveAlongParallel);

        RotateToHeadingCommand rotateToBoilerCommand = rotateToHeadingProvider.get();
        rotateToBoilerCommand.setTargetHeadingProp(angleToBoiler);
        this.addSequential(rotateToBoilerCommand);

        DriveForDistanceCommand driveToBoilerCommand = driveForDistanceProvider.get();
        driveToBoilerCommand.setDeltaDistance(distanceFromTurningPointToBoiler);
        this.addSequential(driveToBoilerCommand);
    }

}
