package competition.subsystems.collector.commands;

import com.google.inject.Inject;

import competition.subsystems.collector.CollectorSubsystem;
import competition.subsystems.collector.CollectorSubsystem.Power;

public class IntakeCollectorCommand extends BaseCollectorCommand {
    
    private Power power = Power.LOW;
    
    @Inject
    public IntakeCollectorCommand(CollectorSubsystem collectorSubsystem) {
        super(collectorSubsystem);
    }
    
    public void setCollectorPower(Power power) {
        this.power = power;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute(){
        collectorSubsystem.intake(power);
    }
}
