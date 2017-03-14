package competition.subsystems.autonomous;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.CalculateDirectBoilerDriveDeltaCommand;
import competition.subsystems.drive.commands.DriveToPointUsingHeuristicsCommand;
import competition.subsystems.vision.commands.RotateRobotToBoilerCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class DriveToBoilerUsingHeuristicsWithVisionCommandGroup extends CommandGroup{

    @Inject
    public DriveToBoilerUsingHeuristicsWithVisionCommandGroup(
            XPropertyManager propMan,
            RotateRobotToBoilerCommand rotateToBoilerCommand,
            CalculateDirectBoilerDriveDeltaCommand calculateCommand,
            DriveToPointUsingHeuristicsCommand driveCommand){
        
        addSequential(rotateToBoilerCommand);
        
        calculateCommand.setChildDriveCommand(driveCommand);
        addSequential(calculateCommand);
        
        addSequential(driveCommand);
    }
}
