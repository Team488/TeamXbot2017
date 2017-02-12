package competition.subsystems.shooter_wheel.commands;

import org.apache.log4j.Logger;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;

public class ContinueShooterCommand extends BaseShooterWheelCommand {  
    
    private static Logger log = Logger.getLogger(ContinueShooterCommand.class);

    public ContinueShooterCommand(ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterWheelSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initialzing ContinueShooterCommand");
    }

    @Override
    public void execute() {
        shooterWheelSubsystem.updatePeriodicData();
    }
}
