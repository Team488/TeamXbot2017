package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.ShootAndDriveAcrossBaseLineCommandGroup;
import competition.subsystems.autonomous.ShootAndActivateHopperCommandGroup;
import competition.subsystems.pose.PoseSubsystem;

public class SetupShootAndActivateHopperCommand extends BaseAutonomousCommandSetter {

    public final ShootAndActivateHopperCommandGroup auto;
    public final PoseSubsystem pose;
    
    @Inject
    public SetupShootAndActivateHopperCommand(
            AutonomousCommandSelector autonomousCommandSelect,
            ShootAndActivateHopperCommandGroup auto,
            PoseSubsystem pose){
        super(autonomousCommandSelect);
        this.pose = pose;
        this.auto = auto;
    }
    
    @Override
    public void initialize(){
        log.info("Initializing");
        log.info("Setting alliance to " + pose.getAllianceColor() + " alliance");
        auto.setAlliance(pose.getAllianceColor());
        this.autonomousCommandSelector.setCurrentAutonomousCommand(auto);
    }

}
