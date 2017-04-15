package competition.subsystems.servo;

import com.google.inject.Inject;

import xbot.common.command.BaseCommand;
import xbot.common.properties.XPropertyManager;

public class SetServoCommand extends BaseCommand {
    private ServoSubsystem servoSubsystem;
    private double targetPose;
    
    @Inject
    public SetServoCommand(XPropertyManager propMan, ServoSubsystem servoSubsystem){
        this.servoSubsystem = servoSubsystem;
        this.requires(servoSubsystem);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        servoSubsystem.setServo(targetPose);
    }
    
    public void setTargetPose(double value) {
        targetPose = value;
    }

}
