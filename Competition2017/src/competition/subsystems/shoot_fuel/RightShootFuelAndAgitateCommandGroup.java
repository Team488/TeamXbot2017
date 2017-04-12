package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.RunIntakeAgitatorIfWheelAtSpeedCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightShootFuelAndAgitateCommandGroup extends CommandGroup {
    protected final RightShootFuelCommandGroup shootRight;
    
    @Inject
    public RightShootFuelAndAgitateCommandGroup(AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            RightShootFuelCommandGroup shootRight,
            ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem) {
        this.shootRight = shootRight;
        
        RunIntakeAgitatorIfWheelAtSpeedCommand intake =
                new RunIntakeAgitatorIfWheelAtSpeedCommand(
                        agitatorsManagerSubsystem.getRightAgitator(), shooterWheelsManagerSubsystem.getRightShooter());
        
        this.addParallel(intake);
        this.addParallel(shootRight);
    }

    public void setShooterRange(TypicalShootingPosition range) {
        shootRight.setShooterRange(range);
    }
}