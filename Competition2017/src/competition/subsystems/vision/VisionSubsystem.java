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
import java.util.Optional;
import java.util.function.Function;

import json.JSONArray;
import json.JSONObject;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XSpeedController;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.XYPair;
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

    private volatile double lastTimePacketRecieved = Timer.getFPGATimestamp();
    private double timeOfReset = 0;
    private double packetsPerSecond;
    private int numberOfPackets = 0;
    private DoubleProperty timeThreshold;
    private boolean isConnected = false;
    private DoubleProperty connectionReportInterval;

    @Inject
    public VisionSubsystem(WPIFactory factory, XPropertyManager propManager) {
        log.info("Creating VisionSubsystem");

        server.setNewPacketHandler(packet -> handleCommPacket(packet));

        server.start();
        log.info("Started server");
        timeThreshold = propManager.createPersistentProperty("The time threshold for the number of packets recieved", 1);
        connectionReportInterval = propManager.createPersistentProperty("the time interval for a report on connection", 5);
    }

    public DetectedLiftPeg getTrackedLiftPeg() {
        return trackedLiftPegs.stream().filter(target -> target.isTracked).findFirst().orElse(null);
    }

    public DetectedBoiler getTrackedBoiler() {
        return trackedBoilers.stream().filter(target -> target.isTracked).findFirst().orElse(null);
    }

    private void handleCommPacket(OffboardCommPacket packet) {
        lastTimePacketRecieved = Timer.getFPGATimestamp() - timeOfReset;
        numberOfPackets++;

        if (packet.packetType.equals(targetSnapshotPacketType)) {
            loadJsonArray(trackedLiftPegs, packet.payload, trackedLiftPegsProperty, jsonTarget -> {
                DetectedLiftPeg newTarget = new DetectedLiftPeg();

                // TODO: Don't assume props exist (check with ".has()")
                // TODO: Move strings to constants
                if(jsonTarget.has("pegOffsetX")){
                    newTarget.pegOffsetX = jsonTarget.getDouble("pegOffsetX");
                } else {
                    newTarget.pegOffsetX = 0;
                }
                
                if(jsonTarget.has("pegOffsetY")){
                    newTarget.pegOffsetY = jsonTarget.getDouble("pegOffsetY");
                } else {
                    newTarget.pegOffsetX = 0;
                }
                
                if(jsonTarget.has("isTracked")){
                    newTarget.isTracked = jsonTarget.getBoolean("isTracked");
                } else{
                    newTarget.isTracked = false;
                }

                return newTarget;
            });

            loadJsonArray(trackedBoilers, packet.payload, trackedBoilersProperty, jsonTarget -> {
                DetectedBoiler newTarget = new DetectedBoiler();

                // TODO: Don't assume props exist (check with ".has()")
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
                    outList.add(objectParser.apply((JSONObject) targetObj));
                }
            }
        }
    }

    @Override
    public void updatePeriodicData() {
        if (lastTimePacketRecieved >= timeThreshold.get() || !isConnected) {
            log.info("Disconnected");
        } else {
            packetsPerSecond = numberOfPackets / lastTimePacketRecieved; 
            numberOfPackets = 0;
            timeOfReset = Timer.getFPGATimestamp();
            log.info(packetsPerSecond + trackedBoilers.size());
        }
        
        if(lastTimePacketRecieved >= connectionReportInterval.get()){
            log.info("Connected");
        }
    }
}
