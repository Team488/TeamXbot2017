package competition.subsystems.shoot_fuel;

import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.commands.RunIntakeAgitatorTracerPowerIfWheelAtSpeedCommand;
import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_belt.commands.RunBeltTracerPowerMode;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class SidedFireTracerRoundsCommandGroup extends CommandGroup {
    
    public SidedFireTracerRoundsCommandGroup(ShooterBeltSubsystem shooterBeltSubsystem,
            AgitatorSubsystem agitatorSubsystem,
            ShooterWheelSubsystem shooterWheelSubsystem) {

        RunBeltTracerPowerMode runTracerPower =
                new RunBeltTracerPowerMode(shooterBeltSubsystem, 
                        shooterWheelSubsystem);
        
        RunIntakeAgitatorTracerPowerIfWheelAtSpeedCommand runAgitator = 
                new RunIntakeAgitatorTracerPowerIfWheelAtSpeedCommand(agitatorSubsystem, 
                        shooterWheelSubsystem);
        
        this.addParallel(runTracerPower);
        this.addParallel(runAgitator);
    }
}
