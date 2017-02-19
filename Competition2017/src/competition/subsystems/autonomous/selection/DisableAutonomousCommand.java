package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

public class DisableAutonomousCommand extends BaseAutonomousCommandSetter {
    
    @Inject
    public DisableAutonomousCommand(AutonomousCommandSelector autonomousCommandSelector) {
        super(autonomousCommandSelector);
    }
    
    @Override
    public void initialize() {
        // set null, causing no program to be run
        autonomousCommandSelector.setCurrentAutonomousCommand(null);
    }

}

