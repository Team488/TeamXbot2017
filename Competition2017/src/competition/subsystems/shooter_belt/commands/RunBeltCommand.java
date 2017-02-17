package competition.subsystems.shooter_belt.commands;

import com.google.inject.Inject;

import competition.subsystems.shooter_belt.ShooterBeltSubsystem;

import xbot.common.command.BaseCommand;

public class RunBeltCommand extends BaseCommand {

    ShooterBeltSubsystem beltSubsystem;
    double targetBeltSpeed;
   
    @Inject
    public RunBeltCommand() {
    }
    
    public void setShooterBeltSubsystem(ShooterBeltSubsystem beltSubsystem) {
        this.beltSubsystem = beltSubsystem;
        this.requires(this.beltSubsystem);
        targetBeltSpeed = beltSubsystem.getTargetSpeed();
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        beltSubsystem.setTargetSpeed(targetBeltSpeed);
    }
}