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
        this.autonomousCommandSelector.setCurrentAutonomousCommand(this.commandProvider.get());
        
    }

    
}
