package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import competition.subsystems.climbing.commands.AscendCommand;
import competition.subsystems.climbing.commands.DescendClimbingCommand;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.agitator.commands.SpinAgitatorBackwardsCommand;
import competition.subsystems.agitator.commands.SpinAgitatorForwardsCommand;
import competition.subsystems.agitator.commands.StopAgitatorCommand;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.ResetDistanceCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class OperatorCommandMap {
    // For mapping operator interface buttons to commands

    // Example for setting up a command to fire when a button is pressed:
    /*
    @Inject
    public void setupMyCommands(
            OperatorInterface operatorInterface,
            MyCommand myCommand)
    {
        operatorInterface.leftButtons.getifAvailable(1).whenPressed(myCommand);
    }
    */
    
    @Inject
    public void setupCollectorCommands(
            OperatorInterface oi,
            EjectCollectorCommand eject,
            IntakeCollectorCommand intake)
    {
        oi.leftButtons.getifAvailable(1).whileHeld(eject);
        oi.leftButtons.getifAvailable(2).whileHeld(intake);
    }
    
    @Inject
    public void setupClimbingCommands(
            OperatorInterface oi,
            DescendClimbingCommand descend,
            AscendCommand ascend
    )   {
        oi.leftButtons.getifAvailable(6).whileHeld(descend);
        oi.leftButtons.getifAvailable(7).whileHeld(ascend);
    }

    public void setupAgitatorCommands(
            OperatorInterface oi,
            SpinAgitatorForwardsCommand forwards,
            SpinAgitatorBackwardsCommand backwards,
            StopAgitatorCommand stop)
    {
        oi.leftButtons.getifAvailable(3).whenPressed(forwards);
        oi.leftButtons.getifAvailable(4).whenPressed(backwards);
        oi.leftButtons.getifAvailable(5).whenPressed(stop);
    }
    
    @Inject
    public void setupDriveCommand(
            DriveForDistanceCommand driveForDistance, 
            ResetDistanceCommand resetDisplacement,
            XPropertyManager propManager)
    {
        DoubleProperty deltaDistance = propManager.createPersistentProperty("Drive for distance test distance", 20);
        resetDisplacement.includeOnSmartDashboard();
        driveForDistance.setDeltaDistance(deltaDistance);
        driveForDistance.includeOnSmartDashboard("Test drive for distance");
    }
}
