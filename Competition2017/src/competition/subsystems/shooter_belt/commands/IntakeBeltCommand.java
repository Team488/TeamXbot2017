package competition.subsystems.shooter_belt.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class IntakeBeltCommand extends BaseCommand {

    ShooterBeltSubsystem beltSubsystem;
    double targetBeltSpeed;
    
    private static Logger log = Logger.getLogger(RunBeltCommand.class);

    @Inject
    public IntakeBeltCommand() {
    }
    
    public void setShooterBeltSubsystem(ShooterBeltSubsystem beltSubsystem) {
        this.beltSubsystem = beltSubsystem;
        this.requires(this.beltSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing IntakeBeltCommand");
    }

    @Override
    public void execute() {
        beltSubsystem.setBeltPower(0.5);
    }
}