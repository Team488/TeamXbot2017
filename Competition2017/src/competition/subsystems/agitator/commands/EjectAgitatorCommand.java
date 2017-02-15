package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.commands.EjectAgitatorCommand;

public class EjectAgitatorCommand extends BaseAgitatorCommand {
        
    public EjectAgitatorCommand(AgitatorSubsystem agitatorSystem) {
        super(agitatorSystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute(){
        agitatorSubsystem.eject();
    }
}