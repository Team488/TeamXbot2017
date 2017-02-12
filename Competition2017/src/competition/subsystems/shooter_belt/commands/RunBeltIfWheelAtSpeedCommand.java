package competition.subsystems.shooter_belt.commands;

import org.apache.log4j.Logger;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
    
public class RunBeltIfWheelAtSpeedCommand extends BaseShooterBeltCommand {

    private static Logger log = Logger.getLogger(RunBeltIfWheelAtSpeedCommand.class);
    
    protected final ShooterWheelSubsystem shooterWheelSubsystem;
    private double targetBeltSpeed;

    public RunBeltIfWheelAtSpeedCommand(ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterBeltSubsystem);
        this.shooterWheelSubsystem = shooterWheelSubsystem;
        targetBeltSpeed = shooterBeltSubsystem.getTargetSpeed();
    }
    
    @Override
    public void initialize() {
        log.info("Initializing " + this.getName());
    }

    @Override
    public void execute() {
        if(shooterWheelSubsystem.isAtSpeed() == true){
            shooterBeltSubsystem.setTargetSpeed(targetBeltSpeed);
        }else{
            shooterBeltSubsystem.setPower(0);
        }
    }  
}
