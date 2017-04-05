package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.BreakBaselineCommandGroup;
import xbot.common.properties.XPropertyManager;

public class SetupBreakBaselineCommand extends BaseAutonomousCommandSetter {

    public final BreakBaselineCommandGroup breakBaselineAuto;
    
    @Inject
    public SetupBreakBaselineCommand(
            AutonomousCommandSelector autonomousCommandSelector,
            BreakBaselineCommandGroup driveForDistance,
            XPropertyManager propMan) {
        super(autonomousCommandSelector);
        this.breakBaselineAuto = driveForDistance;
    }

    @Override
    public void initialize() {
        log.info("Initializing");        
        this.autonomousCommandSelector.setCurrentAutonomousCommand(breakBaselineAuto);
    }

}
