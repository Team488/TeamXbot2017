package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.command.BaseCommand;

public class RunBeltCommand extends BaseCommand {

    final ShooterBeltSubsystem beltSubsystem;

    public RunBeltCommand(ShooterBeltSubsystem beltSubsystem) {

        this.beltSubsystem = beltSubsystem;
        this.requires(this.beltSubsystem);
    }
    
    @Override
    public void initialize() {

    }
    
    @Override
    public void execute() {

        beltSubsystem.updateTelemetry();
    }
}