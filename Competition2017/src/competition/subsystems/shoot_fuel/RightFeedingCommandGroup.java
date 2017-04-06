package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunShooterBeltPowerCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightFeedingCommandGroup extends CommandGroup {

    @Inject
    public RightFeedingCommandGroup( 
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem) {
       
        RunShooterBeltPowerCommand runBelt = 
                new RunShooterBeltPowerCommand(shooterBeltsManagerSubsystem.getRightBelt());
        
        IntakeAgitatorCommand runAgitator = 
                new IntakeAgitatorCommand(agitatorsManagerSubsystem.getRightAgitator());
        
        this.addParallel(runBelt);
        this.addParallel(runAgitator);
    }
}
