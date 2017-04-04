package competition.subsystems.shooter_belt.commands;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
    
public class RunBeltIfWheelAtSpeedCommand extends BaseShooterBeltCommand {
    
    protected final ShooterWheelSubsystem shooterWheelSubsystem;
    protected final DoubleProperty minShooterSpeedForFeeding;

    public RunBeltIfWheelAtSpeedCommand(XPropertyManager propMan, ShooterBeltSubsystem shooterBeltSubsystem, ShooterWheelSubsystem shooterWheelSubsystem) {
        super(shooterBeltSubsystem);
        this.shooterWheelSubsystem = shooterWheelSubsystem;
        minShooterSpeedForFeeding = propMan.createPersistentProperty("Minimum shooter speed for ball feed", 100);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if(Math.abs(shooterWheelSubsystem.getTargetSpeed()) >= minShooterSpeedForFeeding.get() && shooterWheelSubsystem.isWheelAtSpeed()) {
            shooterBeltSubsystem.intakeUsingPower();
        } else {
            shooterBeltSubsystem.setPower(0);
        }
    }  
}
