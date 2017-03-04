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
import xbot.common.controls.actuators.XSpeedController;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.XYPair;
import xbot.common.properties.XPropertyManager;

@Singleton
public class VisionSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(VisionSubsystem.class);

    OffboardCommunicationServer server = new NetworkedCommunicationServer();
    List<DetectedLiftPeg> trackedLiftPegs = Collections.synchronizedList(new ArrayList<>());
    List<DetectedBoiler> trackedBoilers = Collections.synchronizedList(new ArrayList<>());

    private static final String targetSnapshotPacketType = "trackedTargetsSnapshot";
    private static final String trackedLiftPegsProperty = "trackedLiftPegs";
    private static final String trackedBoilersProperty = "trackedBoilers";
    
    @Inject
    public VisionSubsystem(WPIFactory factory, XPropertyManager propManager) {
        log.info("Creating VisionSubsystem");

        server.setNewPacketHandler(packet -> handleCommPacket(packet));
        
        server.start();
        log.info("Started server");
    }
    
    public DetectedLiftPeg getTrackedLiftPeg() {
        return trackedLiftPegs.stream()
                .filter(target -> target.isTracked)
                .findFirst()
                .orElse(null);
    }
    
    public DetectedBoiler getTrackedBoiler() {
        return trackedBoilers.stream()
                .filter(target -> target.isTracked)
                .findFirst()
                .orElse(null);
    }
    
    private void handleCommPacket(OffboardCommPacket packet) {
        // TODO: Only log occasionally, and track disconnects
        log.info(packet.id + " (" + packet.packetType + "): " + packet.payload.toString());
        
        
        if(packet.packetType.equals(targetSnapshotPacketType)) {
            loadJsonArray(trackedLiftPegs, packet.payload, trackedLiftPegsProperty, jsonTarget -> {
                DetectedLiftPeg newTarget = new DetectedLiftPeg();
                
                // TODO: Don't assume props exist (check with ".has()")
                // TODO: Move strings to constants
                
                newTarget.pegOffsetX = jsonTarget.getDouble("pegOffsetX");
                newTarget.pegOffsetY = jsonTarget.getDouble("pegOffsetY");
                
                newTarget.isTracked = jsonTarget.getBoolean("isTracked");
                
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
    
    private <T> void loadJsonArray(List<T> outList, JSONObject payload, String jsonKey, Function<JSONObject, T> objectParser) {
        outList.clear();
        if(payload.has(jsonKey)) {
            JSONArray jsonTargets = payload.getJSONArray(jsonKey);
            for(Object targetObj : jsonTargets) {
                if(JSONObject.class.isInstance(targetObj)) {
                    outList.add(objectParser.apply((JSONObject)targetObj));
                }
            }
        }
    }
}
