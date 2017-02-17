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
            Provider<RotateRobotToBoilerCommand> rotateRobotToBoilerProvider){
        angleToParallel = propMan.createPersistentProperty("Angle to line parallel to boiler", 3);
        distanceFromBoiler = propMan.createPersistentProperty("distance from robot to boiler", 6);
        angleToBoiler = propMan.createPersistentProperty("angle to face boiler", 70);
        distanceFromTurningPointToBoiler = propMan.createPersistentProperty("distance from turning point to boiler", 10);
        
        RotateRobotToBoilerCommand rotateRobotToBoiler1 = rotateRobotToBoilerProvider.get();
        this.addSequential(rotateRobotToBoiler1);

        RotateToHeadingCommand rotateToParallelCommand = rotateToHeadingProvider.get();
        rotateToParallelCommand.setTargetHeadingProp(angleToParallel);
        this.addSequential(rotateToParallelCommand);

        DriveForDistanceCommand driveAlongParallel = driveForDistanceProvider.get();
        driveAlongParallel.setDeltaDistance(distanceFromBoiler.get() * Math.cos(angleToParallel.get()));
        this.addSequential(driveAlongParallel);

        
        RotateToHeadingCommand rotateToSeeBoilerCommand = rotateToHeadingProvider.get();
        rotateToSeeBoilerCommand.setTargetHeadingProp(angleToBoiler);
        this.addSequential(rotateToSeeBoilerCommand);
        
        RotateRobotToBoilerCommand rotateRobotToBoiler2 = rotateRobotToBoilerProvider.get();
        this.addSequential(rotateRobotToBoiler2);

        DriveForDistanceCommand driveToBoilerCommand = driveForDistanceProvider.get();
        driveToBoilerCommand.setDeltaDistance(distanceFromTurningPointToBoiler);
        this.addSequential(driveToBoilerCommand);
    }

}
