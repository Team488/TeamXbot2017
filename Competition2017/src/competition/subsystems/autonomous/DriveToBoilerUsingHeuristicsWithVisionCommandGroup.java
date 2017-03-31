package competition.subsystems.autonomous;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.CalculateDirectBoilerDriveDeltaCommand;
import competition.subsystems.drive.commands.DriveToPointUsingHeuristicsCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.vision.commands.RotateRobotToBoilerCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class DriveToBoilerUsingHeuristicsWithVisionCommandGroup extends CommandGroup{

    @Inject
    public DriveToBoilerUsingHeuristicsWithVisionCommandGroup(
            XPropertyManager propMan,
            RotateRobotToBoilerCommand rotateToBoilerCommand,
            CalculateDirectBoilerDriveDeltaCommand calculateCommand,
            DriveToPointUsingHeuristicsCommand driveCommand,
            ShiftGearCommand shiftCommand){
        shiftCommand.setGear(Gear.LOW_GEAR);
        addSequential(shiftCommand, 0.1);
        
        addSequential(rotateToBoilerCommand);
        
        calculateCommand.setChildDriveCommand(driveCommand);
        addSequential(calculateCommand);
        
        addSequential(driveCommand);
    }
}
