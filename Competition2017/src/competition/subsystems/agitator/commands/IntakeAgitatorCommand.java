package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;

public class IntakeAgitatorCommand extends BaseAgitatorCommand {
    
    public IntakeAgitatorCommand(AgitatorSubsystem agitatorSubsystem) {
        super(agitatorSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute(){
        agitatorSubsystem.intake();
    }    
}