
package competition;

import competition.operator_interface.OperatorCommandMap;
import competition.subsystems.SubsystemDefaultCommandMap;
import competition.subsystems.autonomous.selection.AutonomousCommandSelector;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import xbot.common.command.BaseRobot;
import xbot.common.injection.RobotModule;

public class Robot extends BaseRobot {
    

    AutonomousCommandSelector autonomousCommandSelector;

    @Override
    protected void setupInjectionModule() {
        this.injectionModule = new CompetitionModule();
    }

    @Override
    protected void initializeSystems() {
        super.initializeSystems();
        this.injector.getInstance(SubsystemDefaultCommandMap.class);
        this.injector.getInstance(OperatorCommandMap.class);
        
        // register telemetry sources to be updated even when disabled
        this.registerPeriodicDataSource(this.injector.getInstance(DriveSubsystem.class));
        this.registerPeriodicDataSource(this.injector.getInstance(ShooterWheelsManagerSubsystem.class).getLeftShooter());
        this.registerPeriodicDataSource(this.injector.getInstance(ShooterWheelsManagerSubsystem.class).getRightShooter());
        this.registerPeriodicDataSource(this.injector.getInstance(ShooterBeltsManagerSubsystem.class).getLeftBelt());
        this.registerPeriodicDataSource(this.injector.getInstance(ShooterBeltsManagerSubsystem.class).getRightBelt());
        this.registerPeriodicDataSource(this.injector.getInstance(PoseSubsystem.class));
    }
    
    @Override
    public void autonomousInit() {
        this.autonomousCommand = this.autonomousCommandSelector.getCurrentAutonomousCommand();
        // Base implementation will run the command
        super.autonomousInit();
    }
   
}
