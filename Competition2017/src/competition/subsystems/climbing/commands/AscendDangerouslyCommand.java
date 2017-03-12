package competition.subsystems.climbing.commands;

import com.google.inject.Inject;

import competition.subsystems.climbing.ClimbingSubsystem;

public class AscendDangerouslyCommand extends AscendSafelyCommand {

    @Inject
    public AscendDangerouslyCommand(ClimbingSubsystem climbingSubsystem) {
        super(climbingSubsystem);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        climbingSubsystem.setEnableSafeties(false);
    }

}
