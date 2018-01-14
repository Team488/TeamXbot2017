package competition.subsystems.offboard;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.Quaternion;
import xbot.common.properties.XPropertyManager;

@Singleton
public class OffboardInterfaceSubsystem extends BaseSubsystem implements PeriodicDataSource {
    private static Logger log = Logger.getLogger(OffboardInterfaceSubsystem.class);

    private static final double INCHES_TO_CM = 2.54;
    
    private final DriveSubsystem driveSubsystem;
    private final PoseSubsystem poseSubsystem;
    public final XOffboardCommsInterface rawCommsInterface;

    private Double lastWheelOdomSend = null;
    private double lastLeftDriveDistance;
    private double lastRightDriveDistance;
    
    @Inject
    public OffboardInterfaceSubsystem(WPIFactory factory, XPropertyManager propManager, DriveSubsystem driveSubsystem, PoseSubsystem poseSubsystem, XOffboardCommsInterface commsInterface) {
        log.info("Creating");
        
        this.driveSubsystem = driveSubsystem;
        this.poseSubsystem = poseSubsystem;
        this.rawCommsInterface = commsInterface;
    }
    
    private void sendWheelOdomUpdate() {
        final double timestamp = Timer.getFPGATimestamp();
        
        double leftDriveDistance = this.driveSubsystem.getLeftDistanceInInches();
        double rightDriveDistance = this.driveSubsystem.getRightDistanceInInches();
        
        double leftDriveDelta = (leftDriveDistance - lastLeftDriveDistance) * INCHES_TO_CM;
        double rightDriveDelta = (rightDriveDistance - lastRightDriveDistance) * INCHES_TO_CM;
        
        if (lastWheelOdomSend == null) {
            lastWheelOdomSend = timestamp;
        }
        double timeDelta = timestamp - lastWheelOdomSend;
        lastWheelOdomSend = timestamp;
        
        rawCommsInterface.sendRaw(OffboardCommsConstants.PACKET_TYPE_WHEEL_ODOM, OffboardFramePackingUtils.packWheelOdomFrame(leftDriveDelta, rightDriveDelta, timeDelta));
    }
    
    private void sendOrientationUpdate() {
        Quaternion orientation = poseSubsystem.getImuOrientationQuaternion();
        rawCommsInterface.sendRaw(OffboardCommsConstants.PACKET_TYPE_ORIENTATION, OffboardFramePackingUtils.packOrientationFrame(orientation.w, orientation.x, orientation.y, orientation.z));
    }
    
    private void sendHeadingUpdate() {
        double heading = poseSubsystem.getCurrentHeading().getValue();
        rawCommsInterface.sendRaw(OffboardCommsConstants.PACKET_TYPE_HEADING, OffboardFramePackingUtils.packHeadingFrame(heading));
    }
    
    public void sendSetCurrentCommand(int commandId) {
        rawCommsInterface.sendRaw(OffboardCommsConstants.PACKET_TYPE_SET_CURRENT_COMMAND, OffboardFramePackingUtils.packSetCommandFrame(commandId));
    }

    @Override
    public void updatePeriodicData() {
        sendWheelOdomUpdate();
        sendOrientationUpdate();
        sendHeadingUpdate();
    }
}
