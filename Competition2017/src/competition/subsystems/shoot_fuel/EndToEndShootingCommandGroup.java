package competition.subsystems.shoot_fuel;

import com.google.inject.Inject;

import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class EndToEndShootingCommandGroup extends CommandGroup {
    protected final LeftShootFuelAndAgitateCommandGroup left;
    protected final RightShootFuelAndAgitateCommandGroup right;
    
    @Inject
    public EndToEndShootingCommandGroup(
            LeftShootFuelAndAgitateCommandGroup left,
            RightShootFuelAndAgitateCommandGroup right,
            IntakeCollectorCommand intake) {
        
        this.left = left;
        this.right = right;
        
        this.addParallel(intake);
        this.addParallel(left);
        this.addParallel(right);
    }
    
    public void setShooterRange(TypicalShootingPosition range) {
        left.setShooterRange(range);
        right.setShooterRange(range);
    }
}