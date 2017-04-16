package competition.subsystems.servo;

import com.google.inject.Inject;

import xbot.common.command.BaseCommand;
import xbot.common.properties.XPropertyManager;

public class SetServoCommand extends BaseCommand {
    private final ShooterAimAdjustmentSubsystem servoSubsystem;
    private double targetPose;
    private boolean hasExecuted = false;
    
    @Inject
    public SetServoCommand(XPropertyManager propMan, ShooterAimAdjustmentSubsystem servoSubsystem){
        this.servoSubsystem = servoSubsystem;
        this.requires(servoSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing setServoCommand");
    }

    @Override
    public void execute() {
        servoSubsystem.setServo(targetPose);
        hasExecuted = true;
    }
    
    public void setTargetPose(double value) {
        targetPose = value;
    }
    
    public boolean isFinished(){
        return hasExecuted;
    }

}
