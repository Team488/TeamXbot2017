package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.agitator.commands.SpinAgitatorBackwardsCommand;
import competition.subsystems.agitator.commands.SpinAgitatorForwardsCommand;
import competition.subsystems.agitator.commands.StopAgitatorCommand;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.drive.commands.DriveToPositionCommand;
import competition.subsystems.drive.commands.ResetDisplacementCommand;

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
            DriveToPositionCommand driveToPosition, 
            ResetDisplacementCommand resetDisplacement)
    {
        resetDisplacement.includeOnSmartDashboard();
        driveToPosition.setTargetPosition(24);
        driveToPosition.includeOnSmartDashboard("Drive 24inches");
    }
}
