package competition.subsystems.vision;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.networking.NetworkedCommunicationServer;
import competition.networking.OffboardCommPacket;
import competition.networking.OffboardCommunicationServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import json.JSONArray;
import json.JSONObject;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import edu.wpi.first.wpilibj.Timer;

@Singleton
public class VisionSubsystem extends BaseSubsystem implements PeriodicDataSource {
    private static Logger log = Logger.getLogger(VisionSubsystem.class);

    OffboardCommunicationServer server = new NetworkedCommunicationServer();
    List<DetectedLiftPeg> trackedLiftPegs = Collections.synchronizedList(new ArrayList<>());
    List<DetectedBoiler> trackedBoilers = Collections.synchronizedList(new ArrayList<>());

    private static final String targetSnapshotPacketType = "trackedTargetsSnapshot";
    private static final String trackedLiftPegsProperty = "trackedLiftPegs";
    private static final String trackedBoilersProperty = "trackedBoilers";

    private volatile double lastTimePacketRecieved = -1;
    private volatile double lastPacketCounterResetTime = -1;
    private volatile boolean isConnected = false;
    private volatile int numPacketsSinceReset = 0;
    
    private DoubleProperty connectionTimeoutThreshold;
    private DoubleProperty connectionReportInterval;

    @Inject
    public VisionSubsystem(WPIFactory factory, XPropertyManager propManager) {
        log.info("Creating VisionSubsystem");

        server.setNewPacketHandler(packet -> handleCommPacket(packet));

        server.start();
        log.info("Started server");
        connectionTimeoutThreshold = propManager.createPersistentProperty("Vision connection timeout threshold", 1);
        connectionReportInterval = propManager.createPersistentProperty("Vision telemetry report interval", 20);
    }

    public DetectedLiftPeg getTrackedLiftPeg() {
        return trackedLiftPegs.stream().filter(target -> target.isTracked).findFirst().orElse(null);
    }

    public DetectedBoiler getTrackedBoiler() {
        return trackedBoilers.stream().filter(target -> target.isTracked).findFirst().orElse(null);
    }
    
    public boolean isConnected() {
        return isConnected;
    }

    private void handleCommPacket(OffboardCommPacket packet) {
        lastTimePacketRecieved = Timer.getFPGATimestamp();
        numPacketsSinceReset++;

        if (packet.packetType.equals(targetSnapshotPacketType)) {
            loadJsonArray(trackedLiftPegs, packet.payload, trackedLiftPegsProperty, jsonTarget -> {
              //CHECKSTYLE:OFF
                if(!(
                        jsonTarget.has("pegOffsetX")
                        && jsonTarget.has("pegOffsetY")
                        && jsonTarget.has("isTracked"))) {
                        return null;
                }
                //CHECKSTYLE:ON
                
                DetectedLiftPeg newTarget = new DetectedLiftPeg();

                // TODO: Move strings to constants
                newTarget.pegOffsetX = jsonTarget.getDouble("pegOffsetX");
                newTarget.pegOffsetY = jsonTarget.getDouble("pegOffsetY");
                newTarget.isTracked = jsonTarget.getBoolean("isTracked");

                return newTarget;
            });

            loadJsonArray(trackedBoilers, packet.payload, trackedBoilersProperty, jsonTarget -> {
                //CHECKSTYLE:OFF
                if(!(
                        jsonTarget.has("offsetX")
                        && jsonTarget.has("targetAngleX")
                        && jsonTarget.has("targetAngleY")
                        && jsonTarget.has("isTracked")
                        && jsonTarget.has("distance"))) {
                        return null;
                }
                //CHECKSTYLE:ON
                
                DetectedBoiler newTarget = new DetectedBoiler();

                // TODO: Move strings to constants

                newTarget.offsetX = jsonTarget.getDouble("offsetX");
                newTarget.targetAngleX = jsonTarget.getDouble("targetAngleX");
                newTarget.targetAngleY = jsonTarget.getDouble("targetAngleY");
                newTarget.isTracked = jsonTarget.getBoolean("isTracked");

                newTarget.distance = jsonTarget.getDouble("distance");

                return newTarget;
            });
        }
    }

    private <T> void loadJsonArray(List<T> outList, JSONObject payload, String jsonKey,
            Function<JSONObject, T> objectParser) {
        outList.clear();
        if (payload.has(jsonKey)) {
            JSONArray jsonTargets = payload.getJSONArray(jsonKey);
            for (Object targetObj : jsonTargets) {
                if (JSONObject.class.isInstance(targetObj)) {
                    T newValue = objectParser.apply((JSONObject) targetObj);
                    if(newValue != null) {
                        outList.add(newValue);
                    }
                }
            }
        }
    }

    @Override
    public void updatePeriodicData() {
        double timeSinceLastPacket = lastTimePacketRecieved >= 0 ? Timer.getFPGATimestamp() - lastTimePacketRecieved : 0;
        
        if(timeSinceLastPacket <= connectionTimeoutThreshold.get()) {
            if(!isConnected) {
                log.info("Connected");
                isConnected = true;
            }
            
            double timeSinceLastLog = Timer.getFPGATimestamp() - lastPacketCounterResetTime;
            if(lastPacketCounterResetTime > 0 && timeSinceLastLog >= connectionReportInterval.get()) {
                double packetsPerSecond = numPacketsSinceReset / timeSinceLastLog; 
                numPacketsSinceReset = 0;
                lastPacketCounterResetTime = Timer.getFPGATimestamp();
                
                log.info("Packets per second: " + packetsPerSecond + " (" + trackedBoilers.size() + " tracked boiler(s))");
            }
        }
        else if (isConnected) {
            log.info("Disconnected");
            isConnected = false;
        }
    }
}
