package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveAlongCurveCommand extends BaseDriveCommand {
    
    private final DoubleProperty totalTime;
    private final DoubleProperty timeStep;
    private final DoubleProperty robotTrackWidth;
    
    AutonomousCurveTrajectoryPlanner path;
    
    @Inject
    public DriveAlongCurveCommand
            (DriveSubsystem driveSubsystem,
            XPropertyManager prop
            ) {
        
        super(driveSubsystem);
        this.requires(driveSubsystem);
        
        //Input points to create trajectory for robot
        double[][] waypoints = new double[][]{
            {1,1},
            {2,3},
            {4,5},
            {5,4},
            {7,7}
        };
        
        totalTime = prop.createPersistentProperty("Total time for autonomous command", 10);//seconds
        timeStep = prop.createPersistentProperty("Rate at which controller runs", 0.1);//rate at which rio iterates, seconds
        robotTrackWidth = prop.createPersistentProperty("Distance between left and right wheel", 2); //feet
        
        AutonomousCurveTrajectoryPlanner path = new AutonomousCurveTrajectoryPlanner(waypoints);
    }
    
    @Override
    public void initialize(){
        path.calculate(totalTime.get(), timeStep.get(), robotTrackWidth.get());
    }
    
    @Override
    public void execute(){
        
    }
    
    @Override
    public boolean isFinished(){
        
    }
    
    @Override
    public void end(){
        
    }

}
