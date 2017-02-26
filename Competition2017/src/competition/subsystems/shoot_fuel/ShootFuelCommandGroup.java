package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class ShootFuelCommandGroup extends CommandGroup {

    @Inject
    public ShootFuelCommandGroup(XPropertyManager propManager, 
            RightShootFuelCommandGroup rightShootFuelCommandGroup,
            LeftShootFuelCommandGroup leftShootFuelCommandGroup) {
        this.addParallel(rightShootFuelCommandGroup);
        this.addParallel(leftShootFuelCommandGroup);
    }
}
