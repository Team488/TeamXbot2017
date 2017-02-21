package telemetry;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.properties.BooleanProperty;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class InfluxDBConnection {

    protected final BooleanProperty isEnabledProp;
    protected final StringProperty influxAddressProp;
    
    protected final InfluxDB influxDB;
    protected final String dbName = "XbotRobotData";
    
    @Inject
    public InfluxDBConnection(XPropertyManager propMan) {
        isEnabledProp = propMan.createPersistentProperty("InfluxDB logging enabled", false);
        influxAddressProp = propMan.createPersistentProperty("Influx address", "http://10.4.88.100:8086");
        
        if(isEnabledProp.get()) {
            // current raspberrypi static ip
            influxDB = InfluxDBFactory.connect(influxAddressProp.get(), "root", "root");
            
            int flushEveryPointsCount = 2000;
            int flushEveryMS = 1000;
            influxDB.enableBatch(flushEveryPointsCount, flushEveryMS, TimeUnit.MILLISECONDS);
        } else {
            influxDB = null;
        }
    }
    
    public void writePoint(Point point) {
        if(influxDB != null && isEnabledProp.get()) {
            // autogen is the default retention policy
            influxDB.write(dbName, "autogen", point);
        }
    }
}
