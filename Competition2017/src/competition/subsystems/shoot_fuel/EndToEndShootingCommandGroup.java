package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.collector.commands.IntakeCollectorCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class EndToEndShootingCommandGroup extends CommandGroup {
    
    @Inject
    public EndToEndShootingCommandGroup(LeftShootFuelAndAgitateCommandGroup left,
            RightShootFuelAndAgitateCommandGroup right,
            IntakeCollectorCommand intake) {
                
        this.addParallel(intake);
        this.addParallel(left);
        this.addParallel(right);
    }
}