package competition.subsystems.shoot_fuel;

import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_belt.commands.RunBeltTracerPowerMode;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class FireTracerRoundsCommandGroup extends CommandGroup {
    
    public FireTracerRoundsCommandGroup(ShooterBeltSubsystem shooterBeltSubsystem,
            RunBeltTracerPowerMode runBeltSlowlyUsingPowerCommand,
            AgitatorSubsystem agitatorSubsystem,
            ShooterWheelSubsystem shooterWheelSubsystem) {

        RunBeltTracerPowerMode runTracerPower =
                new RunBeltTracerPowerMode(shooterBeltSubsystem, 
                        shooterWheelSubsystem);
        
        IntakeAgitatorCommand runAgitator = 
                new IntakeAgitatorCommand(agitatorSubsystem, 
                        shooterWheelSubsystem);
        
        this.addParallel(runTracerPower);
        this.addParallel(runAgitator);
    }
}
