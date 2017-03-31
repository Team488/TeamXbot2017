package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class LeftShootFuelAndAgitateCommandGroup extends CommandGroup {
    
    @Inject
    public LeftShootFuelAndAgitateCommandGroup(XPropertyManager propManager, 
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            LeftShootFuelCommandGroup shootLeft) {
        
        IntakeAgitatorCommand intake = new IntakeAgitatorCommand(agitatorsManagerSubsystem.getLeftAgitator(), propManager);
        
        this.addParallel(intake);
        this.addParallel(shootLeft);
    }
}