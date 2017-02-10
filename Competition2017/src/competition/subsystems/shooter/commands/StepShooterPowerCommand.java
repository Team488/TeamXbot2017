package competition.subsystems.shooter.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.shooter.SideShooterSubsystem;
import xbot.common.command.BaseCommand;

public class StepShooterPowerCommand extends BaseCommand {

    SideShooterSubsystem sideShooter;
    
    private static Logger log = Logger.getLogger(StepShooterPowerCommand.class);
    
    @Inject
    public StepShooterPowerCommand(){}
    
    public void setSide(SideShooterSubsystem sideShooter){
        this.sideShooter = sideShooter;
        this.requires(this.sideShooter);
    }

    @Override
    public void initialize() {
        log.info("Changing" + sideShooter.getSide() +  "Shooter's power by" + sideShooter.getPowerStep() );
        sideShooter.setLauncherPower(sideShooter.getLauncherPower()+ sideShooter.getPowerStep());
    }
    
    @Override
    public void execute() {}
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
