package competition.subsystems.shooter_wheel.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;

public class StepShooterPowerCommand extends BaseCommand {

    ShooterWheelSubsystem shooterWheel;
    
    private static Logger log = Logger.getLogger(StepShooterPowerCommand.class);
    
    @Inject
    public StepShooterPowerCommand(){}
    
    public void setSide(ShooterWheelSubsystem shooterWheel){
        this.shooterWheel = shooterWheel;
        this.requires(this.shooterWheel);
    }

    @Override
    public void initialize() {
        log.info("Changing" + shooterWheel.getSide() +  "Shooter's power by" + shooterWheel.getPowerStep() );
        shooterWheel.setLauncherPower(shooterWheel.getLauncherPower()+ shooterWheel.getPowerStep());
    }
    
    @Override
    public void execute() {}
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
