package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.wpi.first.wpilibj.command.Command;

public class GenericAutonomousCommandSetter<T extends Command> extends BaseAutonomousCommandSetter {

    protected final Provider<T> commandProvider;
    
    @Inject
    public GenericAutonomousCommandSetter(
            AutonomousCommandSelector autonomousCommandSelector,
            Provider<T> commandProvider
            ) {
        super(autonomousCommandSelector);
        this.commandProvider = commandProvider;
    }

    @Override
    public void initialize() {
        T autonomousCommand = this.commandProvider.get();
        log.info("Setting autonomous command to " + autonomousCommand.getName());
        this.autonomousCommandSelector.setCurrentAutonomousCommand(autonomousCommand);
        
    }

    
}
