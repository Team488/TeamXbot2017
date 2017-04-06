package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class RunIntakeAgitatorTracerPowerIfWheelAtSpeedCommand extends BaseAgitatorCommand {
    
    protected final ShooterWheelSubsystem shooterWheelSubsystem;
    
    public RunIntakeAgitatorTracerPowerIfWheelAtSpeedCommand(AgitatorSubsystem agitatorSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
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
            agitatorSubsystem.intakeTracerPower();
        } else {
            agitatorSubsystem.stop();
        }
    }
}
