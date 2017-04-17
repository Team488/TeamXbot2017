package competition.subsystems.drive.commands;

import competition.subsystems.drive.DriveSubsystem;

public class StopDriveCommand extends BaseDriveCommand {

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
