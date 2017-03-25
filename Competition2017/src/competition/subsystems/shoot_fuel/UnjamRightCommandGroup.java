package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.EjectAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.EjectBeltCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class UnjamRightCommandGroup extends CommandGroup{

    @Inject
    public UnjamRightCommandGroup(
            AgitatorsManagerSubsystem agitatorsManager,
            ShooterBeltsManagerSubsystem beltsManager
            ) {
        
        EjectBeltCommand reverseBelt = new EjectBeltCommand(beltsManager.getRightBelt());
        EjectAgitatorCommand reverseAgitator = new EjectAgitatorCommand(agitatorsManager.getRightAgitator());
        
        this.addParallel(reverseAgitator);
        this.addParallel(reverseBelt);
    }
}
