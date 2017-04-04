package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.RobotSide;
import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunBeltSlowlyUsingPowerCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class FireTracerRoundsCommandGroup extends CommandGroup {

    private RobotSide side;
    
    @Inject
    public FireTracerRoundsCommandGroup(ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem,
            RunBeltSlowlyUsingPowerCommand runBeltSlowlyUsingPowerCommand,
            AgitatorsManagerSubsystem agitatorsManagerSubsystem,
            ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem) {

        RunBeltSlowlyUsingPowerCommand runTracerPower =
                new RunBeltSlowlyUsingPowerCommand(shooterBeltsManagerSubsystem.getBeltWithRobotSide(side), 
                        shooterWheelsManagerSubsystem.getShooterWheelWithRobotSide(side));
        
        IntakeAgitatorCommand runAgitator = 
                new IntakeAgitatorCommand(agitatorsManagerSubsystem.getAgiatorWithRobotSide(side));
        
        this.addParallel(runTracerPower);
        this.addParallel(runAgitator);
    }
    
    public void setSide(RobotSide side) {
        this.side = side;
    }
}
