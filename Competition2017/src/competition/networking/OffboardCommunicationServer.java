package competition.networking;

import java.util.function.Consumer;

public interface OffboardCommunicationServer {
    public void start();
    public void setNewPacketHandler(Consumer<OffboardCommPacket> handler);
}
