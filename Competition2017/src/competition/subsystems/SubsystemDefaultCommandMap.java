package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.climbing.ClimbingSubsystem;
import competition.subsystems.climbing.RopeAlignerSubsystem;
import competition.subsystems.climbing.commands.StopAligningCommand;
import competition.subsystems.climbing.commands.StopClimbingCommand;
import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.StopAgitatorCommand;
import competition.subsystems.collector.CollectorSubsystem;
import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.StopBeltCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.StopShooterCommand;

@Singleton
public class SubsystemDefaultCommandMap {
    // For setting the default commands on subsystems

    @Inject
    public void setupDriveSubsystem(DriveSubsystem driveSubsystem, TankDriveWithJoysticksCommand command) {
        driveSubsystem.setDefaultCommand(command);
    }

    @Inject
    public void setupShooterWheelSubsystem(ShooterWheelsManagerSubsystem shooterWheelManager) {
        StopShooterCommand stopLeft = new StopShooterCommand(shooterWheelManager.getLeftShooter());
        shooterWheelManager.getLeftShooter().setDefaultCommand(stopLeft);
        
        StopShooterCommand stopRight = new StopShooterCommand(shooterWheelManager.getRightShooter());
        shooterWheelManager.getRightShooter().setDefaultCommand(stopRight);
    }

    @Inject
    public void setupShooterBeltSubsystem(ShooterBeltsManagerSubsystem shooterBeltManager){
        StopBeltCommand stopRight = new StopBeltCommand(shooterBeltManager.getRightBelt());
        shooterBeltManager.getRightBelt().setDefaultCommand(stopRight);
        
        StopBeltCommand stopLeft = new StopBeltCommand(shooterBeltManager.getLeftBelt());
        shooterBeltManager.getLeftBelt().setDefaultCommand(stopLeft);
    }

    @Inject
    public void setupClimbingSubsystem(ClimbingSubsystem climbingSystem, StopClimbingCommand stop) {
        climbingSystem.setDefaultCommand(stop);
    }
    
    @Inject
    public void setupRopeAligningSubsystem(
            RopeAlignerSubsystem aligner,
            StopAligningCommand stop) {
        aligner.setDefaultCommand(stop);
    }
            
    
    @Inject
    public void setupCollectorSubsystem(CollectorSubsystem collectorSystem, StopCollectorCommand stop) {
        collectorSystem.setDefaultCommand(stop);
    }
    
    @Inject
    public void setupAgitatorSubsystem(AgitatorsManagerSubsystem agitatorsManagerSubsystem) {
        StopAgitatorCommand stopLeft = new StopAgitatorCommand(agitatorsManagerSubsystem.getLeftAgitator());
        agitatorsManagerSubsystem.getLeftAgitator().setDefaultCommand(stopLeft);
        
        StopAgitatorCommand stopRight = new StopAgitatorCommand(agitatorsManagerSubsystem.getRightAgitator());
        agitatorsManagerSubsystem.getRightAgitator().setDefaultCommand(stopRight);
    }
}
