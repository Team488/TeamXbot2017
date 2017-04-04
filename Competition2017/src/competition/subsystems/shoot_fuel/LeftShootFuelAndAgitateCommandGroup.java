package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.RunIntakeAgitatorIfWheelAtSpeedCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftShootFuelAndAgitateCommandGroup extends CommandGroup {
    
    @Inject
    public LeftShootFuelAndAgitateCommandGroup(AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            LeftShootFuelCommandGroup shootLeft,
            ShooterWheelsManagerSubsystem shooterBeltsManagerSubsystem) {
        
        RunIntakeAgitatorIfWheelAtSpeedCommand intake = new RunIntakeAgitatorIfWheelAtSpeedCommand(agitatorsManagerSubsystem.getLeftAgitator(),
                shooterBeltsManagerSubsystem.getLeftShooter());
        
        this.addParallel(intake);
        this.addParallel(shootLeft);
    }
}