package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunShooterBeltCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class RightShootFuelCommandGroup extends CommandGroup {

    @Inject
    public RightShootFuelCommandGroup(XPropertyManager propManager, 
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem,
            ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem) {
        IntakeAgitatorCommand intake =
                new IntakeAgitatorCommand(agitatorsManagerSubsystem.getRightAgitator());
        RunShooterWheelsForRangeCommand runWheel = 
                new RunShooterWheelsForRangeCommand(
                        TypicalShootingPosition.FlushToBoiler, 
                        shooterWheelsManagerSubsystem.getRightShooter());
        RunShooterBeltCommand runBelt = 
                new RunShooterBeltCommand(shooterBeltsManagerSubsystem.getRightBelt());
        
        this.addParallel(intake);
        this.addParallel(runWheel);
        this.addParallel(runBelt);
    }
}