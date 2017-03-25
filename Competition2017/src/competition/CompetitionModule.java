package competition;

import competition.networking.NetworkedCommunicationServer;
import competition.networking.OffboardCommunicationServer;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.RobotModule;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class CompetitionModule extends RobotModule {

    @Override
    protected void configure() {
        super.configure();
        this.bind(BasePoseSubsystem.class).to(PoseSubsystem.class);
        this.bind(OffboardCommunicationServer.class).to(NetworkedCommunicationServer.class);
    }
}
