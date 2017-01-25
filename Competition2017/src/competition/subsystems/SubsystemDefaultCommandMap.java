package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.collector.CollectorSubsystem;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;

@Singleton
public class SubsystemDefaultCommandMap {
    // For setting the default commands on subsystems

    @Inject
    public void setupDriveSubsystem(DriveSubsystem driveSubsystem, TankDriveWithJoysticksCommand command) {
        driveSubsystem.setDefaultCommand(command);
    }
    
    @Inject
    public void setupColectorSubsystem(CollectorSubsystem collectorSystem,StopCollectorCommand stop){
        collectorSystem.setDefaultCommand(stop);
    }
}
