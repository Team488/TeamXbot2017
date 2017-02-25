package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.vision.DetectedBoiler;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class CalculateDistanceOfParallelCommand extends BaseCommand{
    private double angleOfLineFromRobotToBoiler;
    private PoseSubsystem pose;
    private VisionSubsystem vision;
    private DoubleProperty angleOfParallel;
    private double distanceOnParallel;
    private DriveForDistanceCommand driveForDistance;

    private boolean isFinished = false;
    
    @Inject
    public CalculateDistanceOfParallelCommand(
            PoseSubsystem pose,
            VisionSubsystem vision,
            XPropertyManager propMan){
        this.pose = pose;
        this.vision = vision;
        angleOfParallel = propMan.createPersistentProperty("Angle of line parallel to boiler", 248);
    }

    @Override
    public void initialize() {
        DetectedBoiler boiler = vision.getTrackedBoiler();
        if(boiler == null) {
            log.warn("Distance calculation failed; no tracked target!");
        }
        else {
            double distanceToBoiler = boiler.distance;
            angleOfLineFromRobotToBoiler = pose.getCurrentHeading().getValue();
            distanceOnParallel = distanceToBoiler * Math.cos((angleOfLineFromRobotToBoiler - angleOfParallel.get()));
            driveForDistance.setDeltaDistance(distanceOnParallel);
            
            isFinished = true;
            
            log.info("Calculated distance to drive: " + distanceOnParallel);
        }
    }

    @Override
    public void execute() {}
    
    @Override
    public boolean isFinished(){
        return isFinished;
    }
    
    public DoubleProperty getAngle(){
        return angleOfParallel;
    }
    
    public void setChildDistanceCommand(DriveForDistanceCommand drive){
        this.driveForDistance = drive;
    }

}
