package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;

public class StopFeedingAndCollectionCommandGroup extends StopAllShootingCommandGroup {

    @Inject
    public StopFeedingAndCollectionCommandGroup(
            StopCollectorCommand stopCollector,
            AgitatorsManagerSubsystem agitatorsManager,
            ShooterBeltsManagerSubsystem beltsManager,
            ShooterWheelsManagerSubsystem wheelsManager) {
        
        super(stopCollector, agitatorsManager, beltsManager, wheelsManager, true);
    }
}
