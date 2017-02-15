package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;

public class StopAgitatorCommand extends BaseAgitatorCommand {
    
    public StopAgitatorCommand(AgitatorSubsystem agitatorSubsystem) {
        super(agitatorSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        agitatorSubsystem.stop();
    }
}
