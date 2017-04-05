package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;

public class UnjamAgitatorCommand extends BaseAgitatorCommand {

    public UnjamAgitatorCommand(AgitatorSubsystem agitatorSubsystem) {
        super(agitatorSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("initializing");
    }

    @Override
    public void execute() {
        
    }

}
