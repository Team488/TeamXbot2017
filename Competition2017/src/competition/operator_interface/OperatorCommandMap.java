package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;


import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.collector.commands.StopCollectorCommand;

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
            IntakeCollectorCommand intake
    )   {
        oi.leftButtons.getifAvailable(1).whileHeld(eject);
        oi.leftButtons.getifAvailable(2).whileHeld(intake);
    }
}
