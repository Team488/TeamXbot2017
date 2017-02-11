
package competition;

import competition.operator_interface.OperatorCommandMap;
import competition.subsystems.SubsystemDefaultCommandMap;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseRobot;

public class Robot extends BaseRobot {

    @Override
    protected void initializeSystems() {
        super.initializeSystems();
        this.injector.getInstance(SubsystemDefaultCommandMap.class);
        this.injector.getInstance(OperatorCommandMap.class);
        
        // register telemetry sources to be updated even when disabled
        this.registerPeriodicDataSource(this.injector.getInstance(DriveSubsystem.class));
        this.registerPeriodicDataSource(this.injector.getInstance(PoseSubsystem.class));
    }
}
