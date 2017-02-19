package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunBeltIfWheelAtSpeedCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
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
