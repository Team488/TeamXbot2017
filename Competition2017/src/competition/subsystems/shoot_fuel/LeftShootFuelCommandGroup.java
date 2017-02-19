package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunBeltIfWheelAtSpeedCommand;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class LeftShootFuelCommandGroup extends CommandGroup {
    
    @Inject
    public LeftShootFuelCommandGroup(XPropertyManager propManager, 
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem,
            ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem) {
        IntakeAgitatorCommand intake = 
                new IntakeAgitatorCommand(agitatorsManagerSubsystem.getLeftAgitator());
        RunShooterWheelsForRangeCommand runWheel = 
                new RunShooterWheelsForRangeCommand(
                        TypicalShootingPosition.FlushToBoiler,
                        shooterWheelsManagerSubsystem.getLeftShooter());
        RunBeltIfWheelAtSpeedCommand runBelt = 
                new RunBeltIfWheelAtSpeedCommand(
                        shooterBeltsManagerSubsystem.getLeftBelt(), 
                        shooterWheelsManagerSubsystem.getLeftShooter());
        
        this.addParallel(intake);
        this.addParallel(runWheel);
        this.addParallel(runBelt);
    }
}