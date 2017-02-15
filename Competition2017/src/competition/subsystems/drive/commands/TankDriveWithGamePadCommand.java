package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;

import xbot.common.command.BaseCommand;

public class TankDriveWithGamePadCommand extends BaseCommand {

        protected final DriveSubsystem driveSubsystem;
        protected final OperatorInterface oi;

        @Inject
        public TankDriveWithGamePadCommand(OperatorInterface oi, DriveSubsystem driveSubsystem) {
            this.oi = oi;
            this.driveSubsystem = driveSubsystem;
            this.requires(this.driveSubsystem);
        }

        @Override
        public void initialize() {
            log.info("Initializing");
        }

        @Override
        public void execute() {
            driveSubsystem.tankDrivePowerMode(oi.controller.getLeftStickY()*-1, oi.controller.getRightStickY()*-1);
        }

    }


