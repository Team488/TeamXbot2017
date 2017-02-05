package competition.subsystems.feeder.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.feeder.SideFeederSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class SetFeederSpeedCommand extends BaseCommand {

    SideFeederSubsystem sideFeeder;
    double targetSpeed;
    
    private static Logger log = Logger.getLogger(SetFeederSpeedCommand.class);

    @Inject
    public SetFeederSpeedCommand(SideFeederSubsystem sideFeeder, XPropertyManager propertyManager) {
        this.sideFeeder = sideFeeder;
        this.requires(this.sideFeeder);
    }
    
    public void setSideFeeder(SideFeederSubsystem sideFeeder) {
        this.sideFeeder = sideFeeder;
    }
    
    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    @Override
    public void initialize() {
        log.info("Initializing SetFeederSpeedCommand");
    }

    @Override
    public void execute() {
        sideFeeder.setFeederTargetSpeed(targetSpeed);
        sideFeeder.updateTelemetry();
    }
}