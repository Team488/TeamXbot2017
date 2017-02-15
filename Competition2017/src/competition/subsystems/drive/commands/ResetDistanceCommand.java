package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;

public class ResetDistanceCommand extends BaseDriveCommand {

    @Inject
    public ResetDistanceCommand(DriveSubsystem driveSubsystem) {
        super(driveSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        driveSubsystem.resetDistance();
    }

    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
