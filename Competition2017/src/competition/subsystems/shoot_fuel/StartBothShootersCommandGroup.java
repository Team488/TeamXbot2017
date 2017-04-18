package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class StartBothShootersCommandGroup extends CommandGroup {
    protected TypicalShootingPosition range = TypicalShootingPosition.FlushToBoiler;
    
    protected final RunShooterWheelsForRangeCommand left;
    protected final RunShooterWheelsForRangeCommand right;
    
    @Inject
    public StartBothShootersCommandGroup(ShooterWheelsManagerSubsystem wheelsManager) {
        
        this.left = new RunShooterWheelsForRangeCommand(range, wheelsManager.getLeftShooter());
        this.right = new RunShooterWheelsForRangeCommand(range, wheelsManager.getRightShooter());
        
        this.addParallel(left);
        this.addParallel(right);
    }
    
    public void setTargetRange(TypicalShootingPosition range) {
        left.setTargetRange(range);
        right.setTargetRange(range);
    }
}