package competition.subsystems.shooter_belt.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.command.BaseCommand;

public class StopBeltCommand extends BaseCommand {

    private static Logger log = Logger.getLogger(StopBeltCommand.class);
    ShooterBeltSubsystem beltSubsystem;

    @Inject
    public StopBeltCommand() {
    }
    
    public void setShooterBeltSubsystem(ShooterBeltSubsystem beltSubsystem) {
        this.beltSubsystem = beltSubsystem;
        this.requires(this.beltSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing " + this.getName());
    }

    @Override
    public void execute() {
        beltSubsystem.setPower(0);
    }
}
