package competition.subsystems.agitator.commands;

import org.apache.log4j.Logger;
import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;

public class IntakeAgitatorCommand extends BaseAgitatorCommand {

    private static Logger log = Logger.getLogger(IntakeAgitatorCommand.class);
    
    public IntakeAgitatorCommand(AgitatorSubsystem agitatorSubsystem) {
        super(agitatorSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing IntakeAgitatorCommand");
    }
    
    @Override
    public void execute(){
        agitatorSubsystem.intake();
    }    
}