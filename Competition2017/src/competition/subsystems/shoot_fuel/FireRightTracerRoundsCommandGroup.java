package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.FireTracerRoundsCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class FireRightTracerRoundsCommandGroup extends CommandGroup {

    @Inject
    public FireRightTracerRoundsCommandGroup(ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem,
            FireTracerRoundsCommand fireTracerRoundsCommand,
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem) {
        
        FireTracerRoundsCommand runTracerPower =
                new FireTracerRoundsCommand(shooterBeltsManagerSubsystem.getRightBelt(), shooterWheelsManagerSubsystem.getRightShooter());
        
        IntakeAgitatorCommand runAgitator = 
                new IntakeAgitatorCommand(agitatorsManagerSubsystem.getRightAgitator());
        
        this.addParallel(runTracerPower);
        this.addParallel(runAgitator);
    }
}
