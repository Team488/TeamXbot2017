package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.shoot_fuel.ShootFuelForNSecondsCommandGroup;

public class SetupShootFuelForNSecondsCommand extends BaseAutonomousCommandSetter {
    public final ShootFuelForNSecondsCommandGroup auto;
    
    @Inject
    public SetupShootFuelForNSecondsCommand(AutonomousCommandSelector autonomousCommandSelector, 
            ShootFuelForNSecondsCommandGroup auto) {
        super(autonomousCommandSelector);
        this.auto = auto;
    }
    
    @Override
    public void initialize(){
        this.autonomousCommandSelector.setCurrentAutonomousCommand(auto);
    }
}
