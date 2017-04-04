package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.RunIntakeAgitatorIfWheelAtSpeedCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightShootFuelAndAgitateCommandGroup extends CommandGroup {
    
    @Inject
    public RightShootFuelAndAgitateCommandGroup(AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            RightShootFuelCommandGroup shootRight,
            ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem) {
        
        RunIntakeAgitatorIfWheelAtSpeedCommand intake = new RunIntakeAgitatorIfWheelAtSpeedCommand(agitatorsManagerSubsystem.getRightAgitator(),
                                                                                                    shooterWheelsManagerSubsystem.getRightShooter());
        
        this.addParallel(intake);
        this.addParallel(shootRight);
    }
}