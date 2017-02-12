package competition.subsystems.shooter_belt.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.command.BaseCommand;

public class RunBeltCommand extends BaseCommand {

    ShooterBeltSubsystem beltSubsystem;
    double targetBeltSpeed;
    
    private static Logger log = Logger.getLogger(RunBeltCommand.class);

    @Inject
    public RunBeltCommand() {
    }
    
    public void setShooterBeltSubsystem(ShooterBeltSubsystem beltSubsystem) {
        this.beltSubsystem = beltSubsystem;
        this.requires(this.beltSubsystem);
        targetBeltSpeed = beltSubsystem.getTargetSpeed();
    }
    
    @Override
    public void initialize() {
        log.info("Initializing " +  this.getName());
    }

    @Override
    public void execute() {
        beltSubsystem.setTargetSpeed(targetBeltSpeed);
    }
}