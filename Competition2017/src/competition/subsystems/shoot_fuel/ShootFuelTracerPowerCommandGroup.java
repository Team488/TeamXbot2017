package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.XPropertyManager;

public class ShootFuelTracerPowerCommandGroup extends CommandGroup {

    @Inject
    public ShootFuelTracerPowerCommandGroup(XPropertyManager propManager, 
            AgitatorsManagerSubsystem agitators,
            ShooterBeltsManagerSubsystem belts,
            ShooterWheelsManagerSubsystem wheels) {

        this.addParallel(new SidedFireTracerRoundsCommandGroup(belts.getLeftBelt(), agitators.getLeftAgitator(), wheels.getLeftShooter()));
        this.addParallel(new SidedFireTracerRoundsCommandGroup(belts.getRightBelt(), agitators.getRightAgitator(), wheels.getRightShooter()));
    }
}
