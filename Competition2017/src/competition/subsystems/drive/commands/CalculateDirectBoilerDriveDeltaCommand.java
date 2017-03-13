package competition.subsystems.drive.commands;

import java.util.function.DoubleSupplier;

import com.google.inject.Inject;

import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.vision.DetectedBoiler;
import competition.subsystems.vision.VisionSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class CalculateDirectBoilerDriveDeltaCommand extends BaseCommand{
    private double angleOfLineFromRobotToBoiler;
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
            angleOfLineFromRobotToBoiler = pose.getCurrentHeading().getValue();
            driveCommand.setDeltaBasedTravel(0, distanceToBoiler, pose.getCurrentHeading().getValue() - 52);
            
            isFinished = true;
            
            log.info("Reported distance to boiler: " + distanceToBoiler);
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
