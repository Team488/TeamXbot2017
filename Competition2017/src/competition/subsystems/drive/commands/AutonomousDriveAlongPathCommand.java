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

public class AutonomousDriveAlongPathCommand extends BaseDriveCommand {
    
    private final DoubleProperty totalTimeOfAutoCurve;
    private final DoubleProperty timeStep;
    private final DoubleProperty robotTrackWidth;
    
    private double[][] waypoints;
    
    private final double initialCommandTime;
    private int indexOfTimeStep;
    
    final AutonomousPathTrajectoryPlanner path;
    
    @Inject
    public AutonomousDriveAlongPathCommand
            (DriveSubsystem driveSubsystem,
            XPropertyManager prop
            ) {
        
        super(driveSubsystem);
        this.requires(driveSubsystem);
        
        this.indexOfTimeStep = 0;
        this.initialCommandTime = Timer.getFPGATimestamp();
        
        //Input points to create trajectory for robot
         this.waypoints = new double[][]{
            {0,10},
            {0,20},
            {0,30},
            {10,30},
            {20,30},
            {30,30}
        };
        
        totalTimeOfAutoCurve = prop.createPersistentProperty("Total time for autonomous curve command", 20);//seconds
        timeStep = prop.createPersistentProperty("Rate at which controller runs", 0.1);//rate at which rio iterates, seconds
        robotTrackWidth = prop.createPersistentProperty("Distance between left and right wheel", 29.25); //inches
        
        path = new AutonomousPathTrajectoryPlanner(waypoints);
    }
    
    public void setWayPoints(double[][] waypoints){
        this.waypoints = waypoints;
    }
    
    public AutonomousPathTrajectoryPlanner getPath(){
        return path;
    }
    
    @Override
    public void initialize(){
        path.calculate(totalTimeOfAutoCurve.get(),
                        timeStep.get(),
                        robotTrackWidth.get());
    }
    
    @Override
    public void execute(){
        if(Timer.getFPGATimestamp()-initialCommandTime >= path.smoothLeftVelocity[indexOfTimeStep][0]){
            driveSubsystem.tankDriveVelocityInchesPerSec
                  (path.smoothLeftVelocity[indexOfTimeStep][1],
                  path.smoothRightVelocity[indexOfTimeStep][1]);
            indexOfTimeStep++;
        }
    }
    
    @Override
    public boolean isFinished(){
        return indexOfTimeStep  == path.smoothLeftVelocity[0].length-1;
    }
    
    @Override
    public void end(){
        log.info("Time taken to complete command:" + (Timer.getFPGATimestamp()-initialCommandTime)); 
    }
}
