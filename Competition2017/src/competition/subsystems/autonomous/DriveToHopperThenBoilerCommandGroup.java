package competition.subsystems.autonomous;

import com.google.inject.Inject;
import com.google.inject.Provider;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveToHopperThenBoilerCommandGroup extends CommandGroup {
    private DoubleProperty distanceFromTurningPointToBoiler;
    private DoubleProperty headingToFaceBoiler;
    private DoubleProperty distanceBackFromHopperToTurningPoint;

    @Inject
    public DriveToHopperThenBoilerCommandGroup(XPropertyManager propManager,
            Provider<DriveForDistanceCommand> driveForDistanceProvider,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider,
            Provider<DriveToHopperCommandGroup> driveToHopperCommandGroupProvider,
            ShiftGearCommand shiftCommand) {
        headingToFaceBoiler = propManager.createPersistentProperty("Heading To Face Boiler In Degees", 70.2);
        distanceFromTurningPointToBoiler = propManager
                .createPersistentProperty("Distance From Turning Point To Boiler In Inches", 72);
        distanceBackFromHopperToTurningPoint = propManager
                .createPersistentProperty("Distance Back From Hopper To Turning Point", -43.25);

        shiftCommand.setGear(Gear.LOW_GEAR);
        this.addSequential(shiftCommand, 0.1);
        
        // drive to hopper from wall
        DriveToHopperCommandGroup driveToHopper = driveToHopperCommandGroupProvider.get();
        this.addSequential(driveToHopper);

        // move back to the turning point on the baseline from the hopper
        DriveForDistanceCommand moveToTurningPointFromHopperCommand = driveForDistanceProvider.get();
        moveToTurningPointFromHopperCommand.setDeltaDistance(distanceBackFromHopperToTurningPoint);
        this.addSequential(moveToTurningPointFromHopperCommand);

        // turn to face the boiler
        RotateToHeadingCommand faceBoilerCommand = rotateToHeadingProvider.get();
        faceBoilerCommand.setTargetHeadingProp(headingToFaceBoiler);
        this.addSequential(faceBoilerCommand);

        // move from the turning point on the baseline to the boiler
        DriveForDistanceCommand moveToBoilerCommand = driveForDistanceProvider.get();
        moveToBoilerCommand.setDeltaDistance(distanceFromTurningPointToBoiler);
        this.addSequential(moveToBoilerCommand);
    }

}
