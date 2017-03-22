package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.ShootAndDriveAcrossBaseLineCommandGroup;
import competition.subsystems.pose.PoseSubsystem;

public class SetupShootAndDriveAcrossBaseLineCommand extends BaseAutonomousCommandSetter {

    public final ShootAndDriveAcrossBaseLineCommandGroup auto;
    public final PoseSubsystem pose;
    
    @Inject
    public SetupShootAndDriveAcrossBaseLineCommand(
            AutonomousCommandSelector autonomousCommandSelect,
            ShootAndDriveAcrossBaseLineCommandGroup auto,
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
