package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunShooterBeltPowerCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftFeedingCommandGroup extends CommandGroup {

    @Inject
    public LeftFeedingCommandGroup( 
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem) {
       
        RunShooterBeltPowerCommand runBelt = 
                new RunShooterBeltPowerCommand(shooterBeltsManagerSubsystem.getLeftBelt());
        
        IntakeAgitatorCommand runAgitator = 
                new IntakeAgitatorCommand(agitatorsManagerSubsystem.getLeftAgitator());
        
        this.addParallel(runBelt);
        this.addParallel(runAgitator);
    }
}
