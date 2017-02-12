package competition.subsystems.shooter_belt.commands;

import org.apache.log4j.Logger;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.command.BaseCommand;

public class RunBeltIfWheelAtSpeedCommand extends BaseCommand {

    private static Logger log = Logger.getLogger(RunBeltIfWheelAtSpeedCommand.class);
    
    final ShooterBeltSubsystem beltSubsystem;
    public ShooterWheelSubsystem shooterWheelSubsystem;
    final double targetBeltSpeed;
    
    public RunBeltIfWheelAtSpeedCommand(ShooterBeltSubsystem beltSubsystem){
        this.beltSubsystem = beltSubsystem;
        this.requires(beltSubsystem);
        targetBeltSpeed = beltSubsystem.getBeltTargetSpeed();
    }
    
    @Override
    public void initialize() {
        log.info("Initializing " + this.getName());
    }

    @Override
    public void execute() {
        if(shooterWheelSubsystem.isAtSpeed() == true){
            beltSubsystem.setBeltTargetSpeed(targetBeltSpeed);
            beltSubsystem.updateTelemetry();
        }else{
            beltSubsystem.setBeltPower(0);
        }
    }  
}
