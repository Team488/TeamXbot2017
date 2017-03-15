package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.vision.DetectedBoiler;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.XPropertyManager;

public class CalculateDirectBoilerDriveDeltaCommand extends BaseCommand{
    private PoseSubsystem pose;
    private VisionSubsystem vision;
    private DriveToPointUsingHeuristicsCommand driveCommand;

    private boolean isFinished = false;
    
    @Inject
    public CalculateDirectBoilerDriveDeltaCommand(
            PoseSubsystem pose,
            VisionSubsystem vision,
            XPropertyManager propMan){
        this.pose = pose;
        this.vision = vision;
    }

    @Override
    public void initialize() {
        DetectedBoiler boiler = vision.getTrackedBoiler();
        if(boiler == null) {
            log.warn("Distance calculation failed; no tracked target!");
        }
        else {
            double distanceToBoiler = boiler.distance;
            double deltaAngle = pose.getCurrentHeading().difference(vision.getHeadingParallelToBoiler() - 90);
            
            driveCommand.setDeltaBasedTravel(0, distanceToBoiler, deltaAngle);
            log.info("Reported distance to boiler: " + distanceToBoiler + "; calculated delta angle: " + deltaAngle);

            isFinished = true;
        }
    }

    @Override
    public void execute() {}
    
    @Override
    public boolean isFinished(){
        return isFinished;
    }
    
    public void setChildDriveCommand(DriveToPointUsingHeuristicsCommand driveCommand){
        this.driveCommand = driveCommand;
    }

}
