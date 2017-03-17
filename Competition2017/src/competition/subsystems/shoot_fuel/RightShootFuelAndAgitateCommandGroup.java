package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class RightShootFuelAndAgitateCommandGroup extends CommandGroup {
    
    @Inject
    public RightShootFuelAndAgitateCommandGroup(XPropertyManager propManager, 
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            RightShootFuelCommandGroup shootRight) {
        
        IntakeAgitatorCommand intake = new IntakeAgitatorCommand(agitatorsManagerSubsystem.getRightAgitator());
        
        this.addParallel(intake);
        this.addParallel(shootRight);
    }
}