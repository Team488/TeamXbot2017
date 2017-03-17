package competition.subsystems.autonomous;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.subsystems.drive.commands.CalculateDistanceOfBoilerParallelCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.DriveInfinitelyCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.vision.VisionSubsystem;
import competition.subsystems.vision.commands.RotateRobotToBoilerCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveToBoilerWithTriangleVisionCommandGroup extends CommandGroup{
    private final DoubleProperty angleToTurnTowardsBoiler;
    private final DoubleProperty boilerApproachPower;

    @Inject
    public DriveToBoilerWithTriangleVisionCommandGroup(
            XPropertyManager propMan,
            VisionSubsystem visionSubsystem,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider,
            DriveForDistanceCommand driveAlongParallel,
            Provider<RotateRobotToBoilerCommand> rotateRobotToBoilerProvider,
            CalculateDistanceOfBoilerParallelCommand calculateParallelDistance,
            DriveInfinitelyCommand infinitelyDriveToBoiler){    
        angleToTurnTowardsBoiler = propMan.createPersistentProperty("Vision boiler scan start angle", 70);
        boilerApproachPower = propMan.createPersistentProperty("Vision boiler approach power", 0.3);
        
        // TODO: Rotate 180 when necessary to ensure we drive forward

        RotateRobotToBoilerCommand rotateRobotToBoiler1 = rotateRobotToBoilerProvider.get();
        RotateRobotToBoilerCommand rotateRobotToBoiler2 = rotateRobotToBoilerProvider.get();
        RotateToHeadingCommand rotateParallelToBoiler = rotateToHeadingProvider.get();
        RotateToHeadingCommand rotateToTurnTowardsBoilerCommand = rotateToHeadingProvider.get();
        
        this.addSequential(rotateRobotToBoiler1);
        
        calculateParallelDistance.setParallelAngleSupplier(() -> visionSubsystem.getHeadingParallelToBoiler());
        calculateParallelDistance.setChildDistanceCommand(driveAlongParallel);
        this.addSequential(calculateParallelDistance);

        rotateParallelToBoiler.setTargetHeadingSupplier(() -> visionSubsystem.getHeadingParallelToBoiler());
        this.addSequential(rotateParallelToBoiler);

        this.addSequential(driveAlongParallel);

        rotateToTurnTowardsBoilerCommand.setTargetHeadingSupplier(() -> angleToTurnTowardsBoiler.get() + visionSubsystem.getHeadingParallelToBoiler());
        this.addSequential(rotateToTurnTowardsBoilerCommand);
        
        this.addSequential(rotateRobotToBoiler2);

        infinitelyDriveToBoiler.setDrivePowerProp(boilerApproachPower);
        this.addSequential(infinitelyDriveToBoiler);
    }
}
