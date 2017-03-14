package competition.subsystems.drive.commands;

import java.util.function.DoubleSupplier;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.vision.DetectedBoiler;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class CalculateDistanceOfBoilerParallelCommand extends BaseCommand{
    private double angleOfLineFromRobotToBoiler;
    private PoseSubsystem pose;
    private VisionSubsystem vision;
    private double distanceOnParallel;
    private DriveForDistanceCommand driveForDistance;
    private DoubleSupplier parallelAngleSupplier;

    private boolean isFinished = false;
    
    @Inject
    public CalculateDistanceOfBoilerParallelCommand(
            PoseSubsystem pose,
            VisionSubsystem vision,
            XPropertyManager propMan){
        this.pose = pose;
        this.vision = vision;
    }
    
    public void setParallelAngle(double angle) {
        parallelAngleSupplier = () -> angle;
    }
    
    public void setParallelAngleProp(DoubleProperty prop) {
        parallelAngleSupplier = () -> prop.get();
    }
    
    public void setParallelAngleSupplier(DoubleSupplier valueSupplier) {
        parallelAngleSupplier = valueSupplier;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        
    }

    @Override
    public void execute() {
        DetectedBoiler boiler = vision.getTrackedBoiler();
        if(boiler == null) {
            log.warn("Distance calculation failed; no tracked target!");
        }
        else {
            double distanceToBoiler = boiler.distance;
            angleOfLineFromRobotToBoiler = pose.getCurrentHeading().getValue();
            distanceOnParallel = distanceToBoiler * Math.cos(Math.toRadians(angleOfLineFromRobotToBoiler - parallelAngleSupplier.getAsDouble()));
            driveForDistance.setDeltaDistance(distanceOnParallel);
            
            isFinished = true;
            
            log.info("Reported distance to boiler: " + distanceToBoiler + "; calculated parallel distance: " + distanceOnParallel);
        }
    }
    
    @Override
    public boolean isFinished(){
        return isFinished;
    }
    
    public void setChildDistanceCommand(DriveForDistanceCommand drive){
        this.driveForDistance = drive;
    }

}
