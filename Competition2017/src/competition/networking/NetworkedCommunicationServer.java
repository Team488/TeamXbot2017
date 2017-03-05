package competition.networking;

import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

import json.JSONObject;

public class NetworkedCommunicationServer implements OffboardCommunicationServer {
    private static Logger log = Logger.getLogger(NetworkedCommunicationServer.class);
    
    private Consumer<OffboardCommPacket> packetHandler;
    private NetworkedServer server;

    private final int connectionPort;
    
    public NetworkedCommunicationServer(int connectionPort) {
        this.connectionPort = connectionPort;
    }
    
    public NetworkedCommunicationServer() {
        this(5800);
    }
    
    @Override
    public void start() {
        if(server != null) {
            log.warn("Server already running; cannot start again.");
            return;
        }
        
        server = new NetworkedServer(connectionPort);
        server.startServer();
        server.setNewPacketHandler(this.packetHandler);
    }

    @Override
    public void setNewPacketHandler(Consumer<OffboardCommPacket> handler) {
        this.packetHandler = handler;
    }
    
    private static class NetworkedServer extends Thread {
        private static Logger log = Logger.getLogger(NetworkedServer.class);
        
        // 8192 is the maximum allowed length; if the packet overflows, the length
        // reported is the max length of the buffer, so the length of the buffer
        // is set to be one greater than the limit to allow for a full 8192 bytes
        // of data before the warning is triggered.
        private static final int RECV_BUFFER_LEN = 8192 + 1;
        
        private static final String packetIdKey = "packetId";
        private static final String packetPayloadKey = "packetPayload";
        private static final String packetTypeKey = "packetType";
        
        private volatile int connectionPort;
        
        private volatile boolean isRunning = false;
        private volatile DatagramSocket serverSocket;
        private volatile Consumer<OffboardCommPacket> packetHandler;
        private volatile boolean lastHandlerState = true;

        public NetworkedServer(int connectionPort) {
            this.connectionPort = connectionPort;
        }

        public void startServer() {
            if(serverSocket != null || this.isRunning) {
                log.error("Server thread already started!");
            }
            
            try {
                serverSocket = new DatagramSocket(this.connectionPort);
                this.isRunning = true;
                this.start();
                log.info("Internal server started");
            } catch (IOException e) {
                log.error("Error starting server:");
                log.error(e.toString());
            }
        }
        
        public void setNewPacketHandler(Consumer<OffboardCommPacket> handler) {
            this.packetHandler = handler;
        }

        public void stopServer() {
            this.isRunning = false;
            serverSocket.close();
        }

        @Override
        public void run() {
            while(isRunning) {
                String dataString = null;
                try {            
                    byte[] receiveBuffer = new byte[RECV_BUFFER_LEN];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    
                    // Block on thread until data are received
                    serverSocket.receive(receivePacket);
                    
                    if(receivePacket.getLength() >= RECV_BUFFER_LEN) {
                        log.warn("Received string longer than buffer! Aborting parse.");
                    }
                    else {
                        dataString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        
                        JSONObject receivedObject = new JSONObject(dataString);
                        if(packetHandler == null){
                            if(lastHandlerState) {
                                log.warn("Received packet but no handler has been registered");
                                lastHandlerState = false;
                            }
                        }
                        else {
                            lastHandlerState = true;
                            
                            OffboardCommPacket packet = new OffboardCommPacket();
                            
                            if(receivedObject.has(packetIdKey) && receivedObject.has(packetTypeKey) && receivedObject.has(packetPayloadKey)) {
                                packet.id = receivedObject.getLong(packetIdKey);
                                packet.packetType = receivedObject.getString(packetTypeKey);
                                packet.payload = receivedObject.getJSONObject(packetPayloadKey);
                                
                                packetHandler.accept(packet);
                            }
                            else {
                                log.error("Malformed packet received! One or more required keys were not present."
                                        + " All packets should have " + packetIdKey + ", " + packetTypeKey + " and " + packetPayloadKey + " keys.");
                            }
                        }
                    }
                }
                catch (EOFException e) {
                    log.error("End of packet reached unexpectedly");
                    log.error(e.toString());
                }
                catch (IOException e) {
                    log.error("IO exception encountered while reading packet");
                    log.error(e.toString());
                }
                catch (json.JSONException e) {
                    log.error("Error parsing JSON string \"" + dataString + "\"");
                    log.error(e.toString());
                }
            }
        }
    }
}
