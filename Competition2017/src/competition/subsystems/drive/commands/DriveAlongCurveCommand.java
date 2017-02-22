package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;

public class DriveAlongCurveCommand extends BaseDriveCommand {
    
    
    @Inject
    public DriveAlongCurveCommand(DriveSubsystem driveSubsystem) {
        super(driveSubsystem);
        
    }
    
    @Override
    public void initialize(){
    
    }
    
    @Override
    public void execute(){
        
    }

}
