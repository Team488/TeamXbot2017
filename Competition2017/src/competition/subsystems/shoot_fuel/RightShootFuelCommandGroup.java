package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunBeltIfWheelAtSpeedCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightShootFuelCommandGroup extends CommandGroup {

    @Inject
    public RightShootFuelCommandGroup(XPropertyManager propManager, 
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
    public RightShootFuelCommandGroup(ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem,
            ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem) {
        RunShooterWheelsForRangeCommand runWheel =
                new RunShooterWheelsForRangeCommand(
                        TypicalShootingPosition.FlushToBoiler, 
                        shooterWheelsManagerSubsystem.getRightShooter());
        RunBeltIfWheelAtSpeedCommand runBelt =
                new RunBeltIfWheelAtSpeedCommand(shooterBeltsManagerSubsystem.getRightBelt(),
                        shooterWheelsManagerSubsystem.getRightShooter());

        this.addParallel(runWheel);
        this.addParallel(runBelt);
    }
}