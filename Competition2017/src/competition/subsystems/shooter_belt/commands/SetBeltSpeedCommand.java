package competition.subsystems.shooter_belt.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class SetBeltSpeedCommand extends BaseCommand {

    ShooterBeltSubsystem beltSubsystem;
    double targetSpeed;
    
    private static Logger log = Logger.getLogger(SetBeltSpeedCommand.class);

    @Inject
    public SetBeltSpeedCommand(ShooterBeltSubsystem beltSubsystem, XPropertyManager propertyManager) {
        this.beltSubsystem = beltSubsystem;
        this.requires(this.beltSubsystem);
    }
    
    public void setShooterBeltSubsystem(ShooterBeltSubsystem beltSubsystem) {
        this.beltSubsystem = beltSubsystem;
    }
    
    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    @Override
    public void initialize() {
        log.info("Initializing SetBeltSpeedCommand");
    }

    @Override
    public void execute() {
        beltSubsystem.setBeltTargetSpeed(targetSpeed);
        beltSubsystem.updateTelemetry();
    }
}