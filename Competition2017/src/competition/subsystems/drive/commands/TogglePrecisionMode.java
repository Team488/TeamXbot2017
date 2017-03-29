package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public class TogglePrecisionMode extends BaseCommand {
        
        private final DriveSubsystem drive;
    
        @Inject
        public TogglePrecisionMode(DriveSubsystem driveSubsystem) {
            drive = driveSubsystem;
        }

        @Override
        public void initialize() {
            log.info("Initializing");
            drive.togglePrecisionMode();

        }

        @Override
        public void execute() {
        }
        
        @Override
        public boolean isFinished(){
            return true;
        }
    }

