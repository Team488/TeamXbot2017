package competition.subsystems.vision;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XDigitalOutput;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.Quaternion;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANInvalidBufferException;
import edu.wpi.first.wpilibj.can.CANJNI;
import edu.wpi.first.wpilibj.can.CANMessageNotAllowedException;
import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;

@Singleton
public class VisionSubsystem extends BaseSubsystem implements PeriodicDataSource {
    private static Logger log = Logger.getLogger(VisionSubsystem.class);

    private static final double INCHES_TO_CM = 2.54;
    
    private DriveSubsystem driveSubsystem;
    private PoseSubsystem poseSubsystem;
    
    private double lastTimePacketRecieved = -1;
    private double lastPacketCounterResetTime = -1;
    private int numPacketsSinceReset = 0;

    private boolean isConnected = false;
    
    private DoubleProperty connectionTimeoutThreshold;
    private DoubleProperty connectionReportInterval;
    
    private BooleanProperty isGettingJetsonData;

    private static final int CAN_ARBID_ROOT = 0x1E040000;
    private static final int CAN_ARBID_ROOT_MASK = 0xFFFF0000;
    private static final int CAN_ARBID_ROOT_AND_SOURCE_MASK = 0xFFFFFF00;

    private static final byte PACKET_TYPE_WHEEL_ODOM = 0x01;
    private static final byte PACKET_TYPE_ORIENTATION = 0x02;

    private Double lastWheelOdomSend = null;
    private double lastLeftDriveDistance;
    private double lastRightDriveDistance;
    
    @Inject
    public VisionSubsystem(WPIFactory factory, XPropertyManager propManager, DriveSubsystem driveSubsystem, PoseSubsystem poseSubsystem) {
        log.info("Creating");
        
        this.driveSubsystem = driveSubsystem;
        this.poseSubsystem = poseSubsystem;
        
        connectionTimeoutThreshold = propManager.createPersistentProperty("Vision connection timeout threshold", 1);
        connectionReportInterval = propManager.createPersistentProperty("Vision telemetry report interval", 20);
        
        isGettingJetsonData = propManager.createEphemeralProperty("Is getting Jetson data", false);
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    
    public void sendRaw(byte packetType, byte[] data) {
        int arbitrationId = CAN_ARBID_ROOT;
        arbitrationId |= 1 << 8; // This packet came from the RIO
        arbitrationId |= packetType;
        
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        buffer.put(data);
        CANJNI.FRCNetCommCANSessionMuxSendMessage(arbitrationId, buffer, CANJNI.CAN_SEND_PERIOD_NO_REPEAT);
    }

    public class CustomCanPacket {
        public final byte packetType;
        public final byte[] data;
        
        public CustomCanPacket(byte packetType, byte[] data) {
            this.packetType = packetType;
            this.data = data;
        }
    }
    
    public CustomCanPacket receiveRaw() {
        ByteBuffer messageIdBuffer = ByteBuffer.allocateDirect(4);
        messageIdBuffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer messageIdIntBuffer = messageIdBuffer.asIntBuffer();
        messageIdIntBuffer.put(CAN_ARBID_ROOT);
        
        // A buffer is required, but we don't care about the timestamp value.
        ByteBuffer timeStamp = ByteBuffer.allocateDirect(4);
                
        try {
            ByteBuffer resultBuf = CANJNI.FRCNetCommCANSessionMuxReceiveMessage(messageIdIntBuffer, CAN_ARBID_ROOT_AND_SOURCE_MASK, timeStamp);
            
            byte[] resultBytes = new byte[resultBuf.remaining()];
            resultBuf.get(resultBytes);
            
            int messageId = messageIdIntBuffer.get();
            byte packetType = (byte)(messageId & (~CAN_ARBID_ROOT_AND_SOURCE_MASK));
            
            return new CustomCanPacket(packetType,  resultBytes);
        }
        catch (CANMessageNotFoundException e) {
            return null;
        }
        catch(CANInvalidBufferException|CANMessageNotAllowedException|CANNotInitializedException ex) {
            log.error("Exception encountered while receiving from CAN device", ex);
            return null;
        }
    }
    
    public static byte[] packWheelOdomFrame(double leftDriveDelta, double rightDriveDelta, double timeDelta) {
        short leftDriveDeltaInteger = (short)(leftDriveDelta * 1_000);
        short rightDriveDeltaInteger = (short)(rightDriveDelta * 1_000);
        // TODO: check for time overflow
        short timeDeltaInteger = (short)(timeDelta * 10_000);
        
        return new byte[] {
            (byte)(leftDriveDeltaInteger >>> 8),
            (byte)(leftDriveDeltaInteger & 0xFF),
            (byte)(rightDriveDeltaInteger >>> 8),
            (byte)(rightDriveDeltaInteger & 0xFF),
            (byte)(timeDeltaInteger >>> 8),
            (byte)(timeDeltaInteger & 0xFF)
        };
    }
    
    public static byte[] packOrientationFrame(float w, float x, float y, float z) {
        short wInteger = (short)(w * 10_000);
        short xInteger = (short)(x * 10_000);
        short yInteger = (short)(y * 10_000);
        short zInteger = (short)(z * 10_000);
        
        return new byte[] {
            (byte)(wInteger >>> 8),
            (byte)(wInteger & 0xFF),
            (byte)(xInteger >>> 8),
            (byte)(xInteger & 0xFF),
            (byte)(yInteger >>> 8),
            (byte)(yInteger & 0xFF),
            (byte)(zInteger >>> 8),
            (byte)(zInteger & 0xFF)
        };
    }
    
    private void sendWheelOdomUpdate() {
        final double timestamp = Timer.getFPGATimestamp();
        
        double leftDriveDistance = this.driveSubsystem.getLeftDistanceInInches();
        double rightDriveDistance = this.driveSubsystem.getRightDistanceInInches();
        
        double leftDriveDelta = (leftDriveDistance - lastLeftDriveDistance) * INCHES_TO_CM;
        double rightDriveDelta = (rightDriveDistance - lastRightDriveDistance) * INCHES_TO_CM;
        
        double timeDelta = timestamp - lastWheelOdomSend;
        lastWheelOdomSend = timestamp;
        
        sendRaw(PACKET_TYPE_WHEEL_ODOM, packWheelOdomFrame(leftDriveDelta, rightDriveDelta, timeDelta));
    }
    
    private void sendOrientationUpdate() {
        Quaternion orientation = poseSubsystem.getImuOrientationQuaternion();
        sendRaw(PACKET_TYPE_ORIENTATION, packOrientationFrame(orientation.w, orientation.x, orientation.y, orientation.z));
    }

    @Override
    public void updatePeriodicData() {
        sendWheelOdomUpdate();
        sendOrientationUpdate();
        /*
        double timeSinceLastPacket = lastTimePacketRecieved >= 0 ? Timer.getFPGATimestamp() - lastTimePacketRecieved : 0;
        
        if(timeSinceLastPacket > 0 && timeSinceLastPacket <= connectionTimeoutThreshold.get()) {
            if(!isConnected) {
                log.info("Connected");
                
                isConnected = true;
                isGettingJetsonData.set(true);
            }
            
            double timeSinceLastLog = Timer.getFPGATimestamp() - lastPacketCounterResetTime;
            if(lastPacketCounterResetTime < 0 || timeSinceLastLog >= connectionReportInterval.get()) {
                double packetsPerSecond = numPacketsSinceReset / timeSinceLastLog; 
                numPacketsSinceReset = 0;
                lastPacketCounterResetTime = Timer.getFPGATimestamp();
                
                log.info("Packets per second: " + packetsPerSecond + " (" + trackedBoilers.size() + " tracked boiler(s))");
            }
        }
        else if (isConnected) {
            trackedBoilers.clear();
            trackedLiftPegs.clear();
            log.info("Disconnected");
            
            isConnected = false;
            isGettingJetsonData.set(false);
        }
        */
    }
}
