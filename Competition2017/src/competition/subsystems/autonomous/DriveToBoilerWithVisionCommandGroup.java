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
    private final DoubleProperty angleToTurnTowardsBoiler;
    private final DoubleProperty boilerApproachPower;
    private final DoubleProperty angleOfParallel;

    @Inject
    public DriveToBoilerWithVisionCommandGroup(
            XPropertyManager propMan,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider,
            Provider<DriveForDistanceCommand> driveForDistanceProvider,
            Provider<RotateRobotToBoilerCommand> rotateRobotToBoilerProvider,
            Provider<CalculateDistanceOfBoilerParallelCommand> calculateParallelDistanceProvider,
            Provider<DriveInfinitelyCommand> driveInfintelyProvider){    
        angleToTurnTowardsBoiler = propMan.createPersistentProperty("Vision boiler scan start angle", 70);
        boilerApproachPower = propMan.createPersistentProperty("Vision boiler approach power", 0.3);
        angleOfParallel = propMan.createPersistentProperty("Angle of line parallel to boiler", 248);
        
        // TODO: Rotate 180 when necessary to ensure we drive forward
        
        RotateRobotToBoilerCommand rotateRobotToBoiler = rotateRobotToBoilerProvider.get();
        CalculateDistanceOfBoilerParallelCommand findDistanceOfParallel = calculateParallelDistanceProvider.get();
        RotateToHeadingCommand rotateParallelToBoiler = rotateToHeadingProvider.get();
        DriveForDistanceCommand driveAlongParallel = driveForDistanceProvider.get();
        RotateToHeadingCommand rotateToTurnTowardsBoilerCommand = rotateToHeadingProvider.get();
        DriveInfinitelyCommand infinitelyDriveToBoilerCommand = driveInfintelyProvider.get();
        
        this.addSequential(rotateRobotToBoiler);
        
        findDistanceOfParallel.setParallelAngleProp(angleOfParallel);
        findDistanceOfParallel.setChildDistanceCommand(driveAlongParallel);
        this.addSequential(findDistanceOfParallel);

        rotateParallelToBoiler.setTargetHeadingProp(angleOfParallel);
        this.addSequential(rotateParallelToBoiler);

        this.addSequential(driveAlongParallel);

        rotateToTurnTowardsBoilerCommand.setTargetHeadingSupplier(() -> angleToTurnTowardsBoiler.get() + angleOfParallel.get());
        this.addSequential(rotateToTurnTowardsBoilerCommand);
        
        this.addSequential(rotateRobotToBoiler);

        infinitelyDriveToBoilerCommand.setDrivePowerProp(boilerApproachPower);
        this.addSequential(infinitelyDriveToBoilerCommand);
    }
    

}
