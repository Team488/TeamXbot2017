package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class AutonomousDriveAlongCurveCommand extends BaseDriveCommand {
    
    private final DoubleProperty totalTimeOfAutoCurve;
    private final DoubleProperty timeStep;
    private final DoubleProperty robotTrackWidth;
    
    private double[][] waypoints;
    
    private final double initialCommandTime;
    private int indexOfTimeStep;
    
    final AutonomousCurveTrajectoryPlanner path;
    
    @Inject
    public AutonomousDriveAlongCurveCommand
            (DriveSubsystem driveSubsystem,
            XPropertyManager prop
            ) {
        
        super(driveSubsystem);
        this.requires(driveSubsystem);
        
        this.indexOfTimeStep = 0;
        this.initialCommandTime = Timer.getFPGATimestamp();
        
        //Input points to create trajectory for robot
         this.waypoints = new double[][]{
            {1,1},
            {2,3},
            {4,5},
            {5,4},
            {7,7}
        };
        
        totalTimeOfAutoCurve = prop.createPersistentProperty("Total time for autonomous curve command", 10);//seconds
        timeStep = prop.createPersistentProperty("Rate at which controller runs", 0.1);//rate at which rio iterates, seconds
        robotTrackWidth = prop.createPersistentProperty("Distance between left and right wheel", 2); //inches
        
        path = new AutonomousCurveTrajectoryPlanner(waypoints);
    }
    
    public void setWayPoints(double[][] waypoints){
        this.waypoints = waypoints;
    }
    
    public AutonomousCurveTrajectoryPlanner getPath(){
        return path;
    }
    
    @Override
    public void initialize(){
        path.calculate(totalTimeOfAutoCurve.get(), timeStep.get(), robotTrackWidth.get());
    }
    
    @Override
    public void execute(){
        if(Timer.getFPGATimestamp()-initialCommandTime >= path.smoothLeftVelocity[indexOfTimeStep][0]){
            driveSubsystem.tankDriveVelocityPid(path.smoothLeftVelocity[indexOfTimeStep][1], path.smoothRightVelocity[indexOfTimeStep][1]);
            indexOfTimeStep++;
        }
    }
    
    @Override
    public boolean isFinished(){
        return(indexOfTimeStep  == path.smoothLeftVelocity[0].length-1);
    }
    
    @Override
    public void end(){
        log.info("Time took to complete command:" + (Timer.getFPGATimestamp()-initialCommandTime)); 
                
    }
    
    

}
