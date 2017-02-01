package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public class ResetDisplacementCommand extends BaseCommand {

    private DriveSubsystem driveSubsystem;
    
    @Inject
    public ResetDisplacementCommand(DriveSubsystem driveSubsystem) {
        this.driveSubsystem = driveSubsystem;
    }
    
    @Override
    public void initialize() {
        driveSubsystem.resetDisplacement();
    }

    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
