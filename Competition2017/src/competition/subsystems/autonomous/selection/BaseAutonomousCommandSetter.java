package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import xbot.common.command.BaseCommand;

public abstract class BaseAutonomousCommandSetter extends BaseCommand {
    protected final AutonomousCommandSelector autonomousCommandSelector;
    
    @Inject
    public BaseAutonomousCommandSetter(AutonomousCommandSelector autonomousCommandSelector) {
        this.autonomousCommandSelector = autonomousCommandSelector;
        this.requires(this.autonomousCommandSelector);
        this.setRunWhenDisabled(true);
    }
    
    @Override
    public void execute() {
        // No operations, all work should be done in initialize of subclasses
    }

    @Override
    public boolean isFinished() {
        // All of these commands should run only once.
        return true;
    }
}
