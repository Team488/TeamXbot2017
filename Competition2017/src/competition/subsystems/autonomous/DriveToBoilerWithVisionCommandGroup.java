package competition.subsystems.autonomous;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.subsystems.drive.commands.CalculateDistanceOfParallelCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.DriveInfinitelyCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.vision.commands.RotateRobotToBoilerCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveToBoilerWithVisionCommandGroup extends CommandGroup{
    private DoubleProperty angleToTurnTowardsBoiler;

    @Inject
    public DriveToBoilerWithVisionCommandGroup(
            XPropertyManager propMan,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider,
            Provider<DriveForDistanceCommand> driveForDistanceProvider,
            Provider<RotateRobotToBoilerCommand> rotateRobotToBoilerProvider,
            Provider<CalculateDistanceOfParallelCommand> calculateParallelDistanceProvider,
            Provider<DriveInfinitelyCommand> driveInfintelyProvider){        
        RotateRobotToBoilerCommand rotateRobotToBoiler1 = rotateRobotToBoilerProvider.get();
        this.addSequential(rotateRobotToBoiler1);
        
        CalculateDistanceOfParallelCommand findAngleAndDistanceOfParallel = calculateParallelDistanceProvider.get();
        DriveForDistanceCommand driveAlongParallel = driveForDistanceProvider.get();
        findAngleAndDistanceOfParallel.setChildDistanceCommand(driveAlongParallel);
        this.addSequential(findAngleAndDistanceOfParallel);

        RotateToHeadingCommand rotateParallelToBoiler = rotateToHeadingProvider.get();
        rotateParallelToBoiler.setTargetHeadingProp(findAngleAndDistanceOfParallel.getAngle());
        this.addSequential(rotateParallelToBoiler);

        this.addSequential(driveAlongParallel);

        angleToTurnTowardsBoiler = propMan.createPersistentProperty("angle to turn to put the boiler within the field of view", 70);
        RotateToHeadingCommand rotateToTurnTowardsBoilerCommand = rotateToHeadingProvider.get();
        rotateToTurnTowardsBoilerCommand.setTargetHeadingProp(angleToTurnTowardsBoiler);
        this.addSequential(rotateToTurnTowardsBoilerCommand);
        
        RotateRobotToBoilerCommand rotateRobotToBoiler2 = rotateRobotToBoilerProvider.get();
        this.addSequential(rotateRobotToBoiler2);

        DriveInfinitelyCommand infinitelyDriveToBoilerCommand = driveInfintelyProvider.get();
        this.addSequential(infinitelyDriveToBoilerCommand);
    }
    

}
