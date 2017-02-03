package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.climbing.ClimbingSubsystem;
import competition.subsystems.climbing.commands.StopClimbingCommand;
import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.commands.StopAgitatorCommand;
import competition.subsystems.collector.CollectorSubsystem;
import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.shooter.ShooterSubsystem;
import competition.subsystems.shooter.SideShooterSubsystem;
import competition.subsystems.shooter.commands.ContinueShooterActionCommand;
import competition.subsystems.shooter.commands.StopShooterCommand;

@Singleton
public class SubsystemDefaultCommandMap {
    // For setting the default commands on subsystems

    @Inject
    public void setupDriveSubsystem(DriveSubsystem driveSubsystem, TankDriveWithJoysticksCommand command) {
        driveSubsystem.setDefaultCommand(command);
    }
    
    @Inject

    public void setupFuelLauncherSubsystem(ShooterSubsystem fuelLauncher, ContinueShooterActionCommand command) {
        fuelLauncher.setDefaultCommand(command);
    }
    
    public void setupClimbingSubsystem(ClimbingSubsystem climbingSystem,StopClimbingCommand stop) {
        climbingSystem.setDefaultCommand(stop);
    }
    
    @Inject
        public void setupCollectorSubsystem(CollectorSubsystem collectorSystem,StopCollectorCommand stop) {
        collectorSystem.setDefaultCommand(stop);
    }
    
    @Inject
    public void setupAgitatorSubsystem(AgitatorSubsystem agitatorSubsystem, StopAgitatorCommand stop) {
        agitatorSubsystem.setDefaultCommand(stop);
    }
}
