package competition.subsystems.drive.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import xbot.common.command.BaseCommand;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDManager;
import xbot.common.properties.XPropertyManager;
import competition.subsystems.drive.DriveSubsystem;

public class RotateToHeadingCommand extends BaseCommand{

    final DriveSubsystem drive;
    ContiguousHeading targetHeading;
    ContiguousHeading currentHeading;
    
    public final double defaultPValue = 1/80d;
    
    private final PIDManager headingDrivePid;
    
    private static Logger log = Logger.getLogger(RotateToHeadingCommand.class);
    
    @Inject
    public RotateToHeadingCommand(DriveSubsystem driveSubsystem, XPropertyManager propMan) {

        this.drive = driveSubsystem;
        headingDrivePid = new PIDManager("Rotate to heading", propMan, defaultPValue, 0, 0);
        
        // Default values - under the hood they are properties, so they can be changed
        // on the SmartDashboard at runtime.
        headingDrivePid.setErrorThreshold(3); // error in degrees, since degrees are used for the calculate call in execute()
        headingDrivePid.setDerivativeThreshold(3.0 / 20); // 3 degrees per second, divided by 20, since derivative is calculated 20 times a second
    }

    public void setTargetHeading(double heading) {
        targetHeading = new ContiguousHeading(heading);
        log.info("Setting target heading to " + heading);
    }
    
    public void reset(){
        log.info("Resetting PID");
        headingDrivePid.reset();
    }
    
    @Override
    public void initialize() {
        log.info("Initializing RotateToHeadingCommand");
        reset();
    }

    @Override
    public void execute() {
        
        double errorInDegrees = drive.imu.getYaw().difference(targetHeading);
        double rotationalPower = headingDrivePid.calculate(0, errorInDegrees);

        drive.tankDrivePowerMode(rotationalPower, -rotationalPower);
    }
    
    @Override
    public boolean isFinished() {
        return headingDrivePid.isOnTarget();
    }
}