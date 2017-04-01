package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunBeltIfWheelAtSpeedCommand;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftShootFuelCommandGroup extends CommandGroup {
    
    @Inject
    public LeftShootFuelCommandGroup(ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem,
            ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem) {
        RunShooterWheelsForRangeCommand runWheel =
                new RunShooterWheelsForRangeCommand(
                        TypicalShootingPosition.FlushToBoiler,
                        shooterWheelsManagerSubsystem.getLeftShooter());

        RunBeltIfWheelAtSpeedCommand runBelt =
                new RunBeltIfWheelAtSpeedCommand(shooterBeltsManagerSubsystem.getLeftBelt(),
                        shooterWheelsManagerSubsystem.getLeftShooter());

        this.addParallel(runWheel);
        this.addParallel(runBelt);
    }
}