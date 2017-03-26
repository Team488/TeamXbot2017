package competition.subsystems.autonomous;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.subsystems.drive.commands.CalculateDistanceOfBoilerParallelCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.DriveInfinitelyCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.vision.VisionSubsystem;
import competition.subsystems.vision.commands.RotateRobotToBoilerCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveToBoilerWithTriangleVisionCommandGroup extends CommandGroup {
    private final DoubleProperty boilerApproachPower;

    @Inject
    public DriveToBoilerWithTriangleVisionCommandGroup(
            XPropertyManager propMan,
            VisionSubsystem visionSubsystem,
            PoseSubsystem poseSubsystem,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider,
            DriveForDistanceCommand driveAlongParallel,
            Provider<RotateRobotToBoilerCommand> rotateRobotToBoilerProvider,
            CalculateDistanceOfBoilerParallelCommand calculateParallelDistance,
            DriveInfinitelyCommand infinitelyDriveToBoiler,
            ShiftGearCommand shiftCommand){    
        boilerApproachPower = propMan.createPersistentProperty("Vision boiler approach power", 0.3);
        
        // TODO: Rotate 180 when necessary to ensure we drive forward

        RotateRobotToBoilerCommand rotateRobotToBoiler1 = rotateRobotToBoilerProvider.get();
        RotateRobotToBoilerCommand rotateRobotToBoiler2 = rotateRobotToBoilerProvider.get();
        RotateToHeadingCommand rotateParallelToBoiler = rotateToHeadingProvider.get();
        RotateToHeadingCommand rotateToTurnTowardsBoilerCommand = rotateToHeadingProvider.get();
        
        shiftCommand.setGear(Gear.LOW_GEAR);
        this.addSequential(shiftCommand, 0.1);
        
        this.addSequential(rotateRobotToBoiler1);
        
        calculateParallelDistance.setParallelAngleSupplier(() -> poseSubsystem.getHeadingFacingBoiler().shiftValue(90).getValue());
        calculateParallelDistance.setChildDistanceCommand(driveAlongParallel);
        this.addSequential(calculateParallelDistance);

        rotateParallelToBoiler.setTargetHeadingSupplier(() -> poseSubsystem.getHeadingFacingBoiler().shiftValue(90).getValue());
        this.addSequential(rotateParallelToBoiler);

        this.addSequential(driveAlongParallel);

        rotateToTurnTowardsBoilerCommand.setTargetHeadingSupplier(() -> poseSubsystem.getHeadingFacingBoiler().getValue());
        this.addSequential(rotateToTurnTowardsBoilerCommand);
        
        this.addSequential(rotateRobotToBoiler2);

        infinitelyDriveToBoiler.setDrivePowerProp(boilerApproachPower);
        this.addSequential(infinitelyDriveToBoiler);
    }
}
