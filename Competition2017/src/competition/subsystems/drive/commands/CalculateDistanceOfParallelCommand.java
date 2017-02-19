package competition.subsystems.drive.commands;

import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class CalculateDistanceOfParallelCommand extends BaseCommand{
    private double angleOfLineFromRobotToBoiler;
    private PoseSubsystem pose;
    private DoubleProperty angleOfParallel;
    private double distanceOnParallel;
    private double distanceToBoiler;
    private DriveForDistanceCommand driveForDistance;

    public CalculateDistanceOfParallelCommand(
            PoseSubsystem pose,
            XPropertyManager propMan){
        this.pose = pose;
        angleOfParallel = propMan.createPersistentProperty("Angle of line parallel to boiler", 248);
    }

    @Override
    public void initialize() {
        distanceToBoiler = 5.0;
        angleOfLineFromRobotToBoiler = pose.getCurrentHeading().getValue();
        distanceOnParallel = distanceToBoiler * Math.cos((angleOfLineFromRobotToBoiler - angleOfParallel.get()));
        driveForDistance.setDeltaDistance(distanceOnParallel);
    }

    @Override
    public void execute() {}
    
    @Override
    public boolean isFinished(){
        return true;
    }
    
    public DoubleProperty getAngle(){
        return angleOfParallel;
    }
    
    public void setChildDistanceCommand(DriveForDistanceCommand drive){
        this.driveForDistance = drive;
    }

}
