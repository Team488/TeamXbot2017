package competition.subsystems.autonomous;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.subsystems.drive.commands.CalculateDistanceOfBoilerParallelCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.DriveInfinitelyCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.vision.commands.RotateRobotToBoilerCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveToBoilerWithVisionCommandGroup extends CommandGroup{
    private DoubleProperty angleToTurnTowardsBoiler;
    private DoubleProperty boilerApproachPower;

    @Inject
    public DriveToBoilerWithVisionCommandGroup(
            XPropertyManager propMan,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider,
            Provider<DriveForDistanceCommand> driveForDistanceProvider,
            Provider<RotateRobotToBoilerCommand> rotateRobotToBoilerProvider,
            Provider<CalculateDistanceOfBoilerParallelCommand> calculateParallelDistanceProvider,
            Provider<DriveInfinitelyCommand> driveInfintelyProvider){    
        angleToTurnTowardsBoiler = propMan.createPersistentProperty("angle to turn to put the boiler within the field of view", 70);
        boilerApproachPower = propMan.createPersistentProperty("Vision boiler approach power", 0.3);
        
        RotateRobotToBoilerCommand rotateRobotToBoiler1 = rotateRobotToBoilerProvider.get();
        this.addSequential(rotateRobotToBoiler1);
        
        CalculateDistanceOfBoilerParallelCommand findAngleAndDistanceOfParallel = calculateParallelDistanceProvider.get();
        DriveForDistanceCommand driveAlongParallel = driveForDistanceProvider.get();
        findAngleAndDistanceOfParallel.setChildDistanceCommand(driveAlongParallel);
        this.addSequential(findAngleAndDistanceOfParallel);

        RotateToHeadingCommand rotateParallelToBoiler = rotateToHeadingProvider.get();
        rotateParallelToBoiler.setTargetHeadingProp(findAngleAndDistanceOfParallel.getAngle());
        this.addSequential(rotateParallelToBoiler);

        this.addSequential(driveAlongParallel);

        RotateToHeadingCommand rotateToTurnTowardsBoilerCommand = rotateToHeadingProvider.get();
        rotateToTurnTowardsBoilerCommand.setTargetHeadingProp(angleToTurnTowardsBoiler);
        this.addSequential(rotateToTurnTowardsBoilerCommand);
        
        RotateRobotToBoilerCommand rotateRobotToBoiler2 = rotateRobotToBoilerProvider.get();
        this.addSequential(rotateRobotToBoiler2);

        DriveInfinitelyCommand infinitelyDriveToBoilerCommand = driveInfintelyProvider.get();
        // Changing this power property will have no effect until the robot code is reloaded
        infinitelyDriveToBoilerCommand.setDrivePower(boilerApproachPower.get());
        this.addSequential(infinitelyDriveToBoilerCommand);
    }
    

}
