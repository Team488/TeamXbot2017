package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class RunIntakeAgitatorIfWheelAtSpeedCommand extends BaseAgitatorCommand {
    
    protected final ShooterWheelSubsystem shooterWheelSubsystem;
    
    public RunIntakeAgitatorIfWheelAtSpeedCommand(AgitatorSubsystem agitatorSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(agitatorSubsystem);
        this.shooterWheelSubsystem = shooterWheelSubsystem;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if(shooterWheelSubsystem.isWheelAtSpeed()) {
            agitatorSubsystem.intake();
        } else {
            agitatorSubsystem.stop();
        }
    }
}
