package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public class ResetDistanceCommand extends BaseCommand {

    private DriveSubsystem driveSubsystem;
    
    @Inject
    public ResetDistanceCommand(DriveSubsystem driveSubsystem) {
        this.driveSubsystem = driveSubsystem;
    }
    
    @Override
    public void initialize() {
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
