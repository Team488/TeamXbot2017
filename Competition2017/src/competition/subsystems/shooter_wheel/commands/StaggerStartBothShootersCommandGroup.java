package competition.subsystems.shooter_wheel.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class StaggerStartBothShootersCommandGroup extends CommandGroup {
    protected TypicalShootingPosition range = TypicalShootingPosition.FlushToBoiler;
    
    protected final RunShooterWheelsForRangeCommand left;
    protected final RunShooterWheelsForRangeCommand right;
    
    @Inject
    public StaggerStartBothShootersCommandGroup(ShooterWheelsManagerSubsystem wheelsManager) {
        
        this.left = new RunShooterWheelsForRangeCommand(range, wheelsManager.getLeftShooter(), 1);
        this.right = new RunShooterWheelsForRangeCommand(range, wheelsManager.getRightShooter(), 4);
        
        this.addParallel(left);
        this.addParallel(right);
    }
    
    public void setTargetRange(TypicalShootingPosition range) {
        left.setTargetRange(range);
        right.setTargetRange(range);
    }
}