package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;

public class StopDriveCommand extends BaseDriveCommand {

    @Inject
    public StopDriveCommand(DriveSubsystem driveSubsystem) {
        super(driveSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        driveSubsystem.stop();
    }

    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
