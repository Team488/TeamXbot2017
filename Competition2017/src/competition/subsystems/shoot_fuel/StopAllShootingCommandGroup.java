package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.StopAgitatorCommand;
import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.StopBeltCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.StopShooterCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class StopAllShootingCommandGroup extends CommandGroup {

    @Inject
    public StopAllShootingCommandGroup(
            StopCollectorCommand stopCollector,
            AgitatorsManagerSubsystem agitatorsManager,
            ShooterBeltsManagerSubsystem beltsManager,
            ShooterWheelsManagerSubsystem wheelsManager) {
        
        StopAgitatorCommand stopLeftAgitator = new StopAgitatorCommand(agitatorsManager.getLeftAgitator());
        StopAgitatorCommand stopRightAgitator = new StopAgitatorCommand(agitatorsManager.getRightAgitator());
        
        StopBeltCommand stopLeftBelt = new StopBeltCommand(beltsManager.getLeftBelt());
        StopBeltCommand stopRightBelt = new StopBeltCommand(beltsManager.getRightBelt());
        
        StopShooterCommand stopLeftWheel = new StopShooterCommand(wheelsManager.getLeftShooter());
        StopShooterCommand stopRightWheel = new StopShooterCommand(wheelsManager.getRightShooter());
        
        this.addParallel(stopCollector, 0.2);
        
        this.addParallel(stopLeftAgitator, 0.2);
        this.addParallel(stopRightAgitator, 0.2);
        
        this.addParallel(stopLeftBelt, 0.2);
        this.addParallel(stopRightBelt, 0.2);
        
        this.addParallel(stopLeftWheel, 0.2);
        this.addParallel(stopRightWheel, 0.2);
    }
}
