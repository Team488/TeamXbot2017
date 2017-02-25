package competition.subsystems.drive.commands;

import com.google.inject.Inject;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;

public class RotateToHeadingCommand extends BaseDriveCommand {
    
    private final PoseSubsystem poseSubsystem;
    ContiguousHeading targetHeading;
    ContiguousHeading currentHeading;
    DoubleProperty targetHeadingProp;
    
    public final double defaultPValue = 1 / 80d;
    
    private final PIDManager headingDrivePid;
    
    @Inject
    public RotateToHeadingCommand(
            DriveSubsystem driveSubsystem, 
            XPropertyManager propMan,
            PoseSubsystem pose,
            RobotAssertionManager assertionManager)
    {
        super(driveSubsystem);
        this.poseSubsystem = pose;
        headingDrivePid = new PIDManager("Rotate to heading", propMan, assertionManager, defaultPValue, 0, 0);
        
        // Default values - under the hood they are properties, so they can be changed
        // on the SmartDashboard at runtime.
        headingDrivePid.setErrorThreshold(3); // error in degrees, since degrees are used for the calculate call in execute()
        headingDrivePid.setDerivativeThreshold(3.0 / 20); // 3 degrees per second, divided by 20, since derivative is calculated 20 times a second
        
        headingDrivePid.setEnableErrorThreshold(true);
        headingDrivePid.setEnableDerivativeThreshold(true);
    }

    public void setTargetHeading(double heading) {
        targetHeading = new ContiguousHeading(heading);
        log.info("Setting target heading to " + heading);
    }
    
    public void setTargetHeadingProp(DoubleProperty heading){
        targetHeadingProp = heading;
    }
    
    public void reset(){
        log.info("Resetting PID");
        headingDrivePid.reset();
    }
    
    @Override
    public void initialize() {
        log.info("Initializing RotateToHeadingCommand");
        if(targetHeadingProp != null){
            targetHeading = new ContiguousHeading(targetHeadingProp.get());
            log.info("Setting target heading to " + targetHeadingProp.get());
        }
        reset();
    }

    @Override
    public void execute() {
        double errorInDegrees =  poseSubsystem.getCurrentHeading().difference(targetHeading);
        double rotationalPower = headingDrivePid.calculate(errorInDegrees, 0);
        
        driveSubsystem.tankDrivePowerMode(-rotationalPower, rotationalPower);
    }
    
    @Override
    public boolean isFinished() {
        return headingDrivePid.isOnTarget();
    }
    
    @Override 
    public void end(){
        log.info("Ending. Current Heading is " + currentHeading + " and Target Heading was" 
                +targetHeading);
    }
}