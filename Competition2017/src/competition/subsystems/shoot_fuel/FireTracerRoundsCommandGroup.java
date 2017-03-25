package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;
import competition.subsystems.shooter_belt.commands.FireTracerRoundsCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class FireTracerRoundsCommandGroup extends CommandGroup {

    @Inject
    public FireTracerRoundsCommandGroup(FireTracerRoundsCommand fireTracerRoundsCommand,
            ShootFuelCommandGroup shootFuelCommandGroup) {
        this.addParallel(fireTracerRoundsCommand);
    }
}
