package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunShooterBeltPowerCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class RightFeedingCommandGroup extends CommandGroup {

    @Inject
    public RightFeedingCommandGroup( 
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem,
            XPropertyManager propManager) {
       
        RunShooterBeltPowerCommand runBelt = 
                new RunShooterBeltPowerCommand(shooterBeltsManagerSubsystem.getRightBelt());
        
        IntakeAgitatorCommand runAgitator = 
                new IntakeAgitatorCommand(agitatorsManagerSubsystem.getRightAgitator(), propManager);
        
        this.addParallel(runBelt);
        this.addParallel(runAgitator);
    }
}
