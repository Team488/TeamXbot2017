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

public class DriveAlongCurveCommand extends BaseDriveCommand {
    
    private final DoubleProperty totalTime;
    private final DoubleProperty timeStep;
    private final DoubleProperty robotTrackWidth;
    
    private final double initialCommandStart;
    private int i;
    
    AutonomousCurveTrajectoryPlanner path;
    
    @Inject
    public DriveAlongCurveCommand
            (DriveSubsystem driveSubsystem,
            XPropertyManager prop
            ) {
        
        super(driveSubsystem);
        this.requires(driveSubsystem);
        
        this.i = 0;
        this.initialCommandStart = Timer.getFPGATimestamp();
        
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
        if(Timer.getFPGATimestamp()-initialCommandStart == path.smoothLeftVelocity[i][0]){
            driveSubsystem.tankDriveVelocityPid(path.smoothLeftVelocity[i][1], path.smoothRightVelocity[i][1]);
            i++;
        }
    }
    
    @Override
    public boolean isFinished(){
        if(i  == path.smoothLeftVelocity[0].length-1){
            return true;
        }
        return false;
    }
    
    @Override
    public void end(){
        
    }

}
