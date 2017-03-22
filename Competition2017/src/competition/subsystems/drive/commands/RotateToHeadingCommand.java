package competition.subsystems.drive.commands;

import java.util.function.DoubleSupplier;

import com.google.inject.Inject;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;

public class RotateToHeadingCommand extends BaseDriveCommand {
    
    protected final PoseSubsystem poseSubsystem;
    protected ContiguousHeading targetHeading;
    
    protected DoubleSupplier targetHeadingSupplier;
    
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
        targetHeadingSupplier = () -> heading;
    }
    
    public void setTargetHeadingProp(DoubleProperty heading){
        targetHeadingSupplier = () -> heading.get();
    }
    
    public void setTargetHeadingSupplier(DoubleSupplier headingSupplier) {
        targetHeadingSupplier = headingSupplier;
    }
    
    public void reset(){
        log.info("Resetting PID");
        headingDrivePid.reset();
    }
    
    @Override
    public void initialize() {
        if(targetHeadingSupplier == null) {
            log.error("RotateToHeadingCommand initialized without a heading setpoint!");
            targetHeading = null;
            return;
        }
        
        double currentTarget = targetHeadingSupplier.getAsDouble();
        targetHeading = new ContiguousHeading(currentTarget);
        
        log.info("Initializing RotateToHeadingCommand with target " + currentTarget + " (" + targetHeading + ")");
        reset();
    }

    @Override
    public void execute() {
        if(targetHeading != null) {
            double errorInDegrees =  poseSubsystem.getCurrentHeading().difference(targetHeading);
            double rotationalPower = headingDrivePid.calculate(0, errorInDegrees);
    
            driveSubsystem.tankDrivePowerMode(rotationalPower, -rotationalPower);
        }
    }
    
    @Override
    public boolean isFinished() {
        return targetHeading != null && headingDrivePid.isOnTarget();
    }
    
    @Override 
    public void end(){
        log.info("Ending. Current Heading is " + poseSubsystem.getCurrentHeading() + " and Target Heading was" 
                + targetHeading);
    }
}