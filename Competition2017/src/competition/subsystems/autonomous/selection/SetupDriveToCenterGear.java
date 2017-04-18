package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.DriveToCenterGearCommandGroup;
import xbot.common.properties.XPropertyManager;

public class SetupDriveToCenterGear extends BaseAutonomousCommandSetter {

    private final DriveToCenterGearCommandGroup autoGearCenter;
    
    @Inject
    public SetupDriveToCenterGear(
            AutonomousCommandSelector autonomousCommandSelector,
            DriveToCenterGearCommandGroup autoGearCenter,
            XPropertyManager propMan) {
        super(autonomousCommandSelector);
        this.autoGearCenter = autoGearCenter;
    }

    @Override
    public void initialize() {
        log.info("Initializing");        
        this.autonomousCommandSelector.setCurrentAutonomousCommand(autoGearCenter);
    }
}
