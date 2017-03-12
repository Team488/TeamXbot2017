package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShootFuelForNSecondsCommandGroup extends CommandGroup {
    private DoubleProperty nSeconds;
    
    @Inject
    public ShootFuelForNSecondsCommandGroup(XPropertyManager propManager, 
            RightShootFuelCommandGroup rightShootFuelCommandGroup,
            LeftShootFuelCommandGroup leftShootFuelCommandGroup) {
        nSeconds = propManager.createPersistentProperty("Autonomous shoot fuel duration", 10);
        this.addParallel(rightShootFuelCommandGroup, nSeconds.get());
        this.addParallel(leftShootFuelCommandGroup, nSeconds.get());
    }
}
