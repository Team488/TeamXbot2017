package competition.subsystems.shoot_fuel;

import competition.subsystems.RobotSide;
import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunBeltSlowlyUsingPowerCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class FireTracerRoundsCommandGroup extends CommandGroup {
    
    public FireTracerRoundsCommandGroup(RobotSide side,
            ShooterBeltsManagerSubsystem shooterBeltsManagerSubsystem,
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
}
