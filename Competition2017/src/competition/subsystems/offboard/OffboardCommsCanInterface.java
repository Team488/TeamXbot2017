package competition.subsystems.offboard;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.apache.log4j.Logger;

import edu.wpi.first.wpilibj.can.CANInvalidBufferException;
import edu.wpi.first.wpilibj.can.CANJNI;
import edu.wpi.first.wpilibj.can.CANMessageNotAllowedException;
import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;

public class OffboardCommsCanInterface implements XOffboardCommsInterface {
    static Logger log = Logger.getLogger(OffboardCommsCanInterface.class);

    public void sendRaw(byte packetType, byte[] data) {
        int arbitrationId = OffboardCommsConstants.CAN_ARBID_ROOT;
        arbitrationId |= 1 << 8; // This packet came from the RIO
        arbitrationId |= packetType;
        
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        buffer.put(data);
        CANJNI.FRCNetCommCANSessionMuxSendMessage(arbitrationId, buffer, CANJNI.CAN_SEND_PERIOD_NO_REPEAT);
    }
    
    public OffboardCommunicationPacket receiveRaw() {
        ByteBuffer messageIdBuffer = ByteBuffer.allocateDirect(4);
        messageIdBuffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer messageIdIntBuffer = messageIdBuffer.asIntBuffer();
        
        // A buffer is required, but we don't care about the timestamp value.
        ByteBuffer timeStamp = ByteBuffer.allocateDirect(4);
        
        try {
            ByteBuffer resultBuf = CANJNI.FRCNetCommCANSessionMuxReceiveMessage(messageIdIntBuffer, OffboardCommsConstants.CAN_ARBID_ROOT_AND_SOURCE_MASK, timeStamp);
            
            byte[] resultBytes = new byte[resultBuf.remaining()];
            resultBuf.get(resultBytes);
            
            int messageId = messageIdIntBuffer.get();
            byte packetType = (byte)(messageId & (~OffboardCommsConstants.CAN_ARBID_ROOT_AND_SOURCE_MASK));
            
            return new OffboardCommunicationPacket(packetType,  resultBytes);
        }
        catch (CANMessageNotFoundException e) {
            return null;
        }
        catch(CANInvalidBufferException|CANMessageNotAllowedException|CANNotInitializedException ex) {
            log.error("Exception encountered while receiving from CAN device", ex);
            return null;
        }
    }
}
