package competition;

import java.util.function.Consumer;

import competition.networking.OffboardCommPacket;
import competition.networking.OffboardCommunicationServer;

public class MockOffboardCommServer implements OffboardCommunicationServer {
    @Override
    public void start() {
        
    }

    @Override
    public void setNewPacketHandler(Consumer<OffboardCommPacket> handler) {
        
    }
}
