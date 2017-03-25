package competition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import edu.wpi.first.wpilibj.Timer;
import xbot.common.properties.DoubleProperty;

public class DiskDeferredTelemetryLogger implements DeferredTelemetryLogger {
    private static String baseTelemetryFileName = "/home/lvuser/488-telemetry/";
    private static double flushInterval = 5;
    
    private Logger log;
    private String name;
    
    private PrintWriter out;
    private DoubleProperty[] properties;
    private double lastFlushTime = 0;
    
    private boolean lastLoggedInitError = false;
    
    @Override
    public void initialize(String name, DoubleProperty... properties) {
        name = (name == null ? "unnamed" : name).replaceAll("\\W", "-");
        this.name = name;
        log = Logger.getLogger("DiskDeferredTelemetryLogger " + name);
        
        try {
            String dataFileName = baseTelemetryFileName + name + ".csv";
            File dataFile = new File(dataFileName);
            dataFile.mkdirs();
            dataFile.delete();
            
            out = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter(dataFile)));
            
            String row = Stream.concat(
                    Stream.of("Time"),
                    Arrays.stream(properties)
                        .map(p -> p.key))
                    .collect(Collectors.joining(","));
            out.println(row);
            
            log.info("Initialized telemetry logger " + name + " (file " + dataFileName + ")");

            lastLoggedInitError = false;
        } catch (IOException e) {
            if(!lastLoggedInitError) {
                log.error("Error while opening output file", e);
            }
            out = null;
            lastLoggedInitError = true;
        }
        
        this.properties = properties.clone();
    }

    @Override
    public void collectNewDataset() {
        if(out != null) {
            String row = Stream.concat(
                    Stream.of(Double.toString(Timer.getFPGATimestamp())),
                    Arrays.stream(properties)
                        .map(p -> Double.toString(p.get())))
                    .collect(Collectors.joining(","));
            out.println(row);
        }
    }

    @Override
    public void updatePeriodicData() {
        if(out != null) {
            if(Timer.getFPGATimestamp() - lastFlushTime > flushInterval) {
                out.flush();
                lastFlushTime = Timer.getFPGATimestamp();
            }
        }
    }

    @Override
    public void uninitialize() {
        if(out != null) {
            out.flush();
            out = null;
            log.info("Uninitialized logger " + name);
        }
    }

    @Override
    public boolean isInitialized() {
        return out != null;
    }
}