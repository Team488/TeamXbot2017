package competition.subsystems.shooter_wheel.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import xbot.common.command.BaseCommand;

public class ContinueShooterCommand extends BaseCommand {
    
    private static Logger log = Logger.getLogger(ContinueShooterCommand.class);
    
    ShooterWheelSubsystem shooterWheel;

    @Inject
    public ContinueShooterCommand() {
    }
    
    public void setSide(ShooterWheelSubsystem shooterWheel) {
        this.shooterWheel = shooterWheel;
        this.requires(shooterWheel);
    }

    @Override
    public void initialize() {
        log.info("Initialzing ContinueShooterCommand");
    }
    
    @Override
    public void execute() {
        shooterWheel.updateTelemetry();
    }
    
}
