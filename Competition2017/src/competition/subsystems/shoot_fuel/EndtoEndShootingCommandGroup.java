package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class EndtoEndShootingCommandGroup extends CommandGroup {
    
    @Inject
    public EndtoEndShootingCommandGroup(XPropertyManager propManager, 
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            LeftShootFuelAndAgitateCommandGroup left,
            RightShootFuelAndAgitateCommandGroup right,
            IntakeCollectorCommand intake) {
                
        this.addParallel(intake);
        this.addParallel(left);
        this.addParallel(right);
    }
}