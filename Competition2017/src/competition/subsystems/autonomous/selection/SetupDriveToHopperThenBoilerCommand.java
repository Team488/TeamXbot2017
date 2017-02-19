package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.DriveToHopperCommandGroup;
import competition.subsystems.autonomous.DriveToHopperThenBoilerCommandGroup;
import xbot.common.properties.DoubleProperty;

public class SetupDriveToHopperThenBoilerCommand extends BaseAutonomousCommandSetter {

    public final DriveToHopperThenBoilerCommandGroup auto;
    
    @Inject
    public SetupDriveToHopperThenBoilerCommand(AutonomousCommandSelector autonomousCommandSelector, 
            DriveToHopperThenBoilerCommandGroup auto) {
        super(autonomousCommandSelector);
        this.auto = auto;
    }
    
    @Override
    public void initialize(){
        this.autonomousCommandSelector.setCurrentAutonomousCommand(auto);
    }

}
