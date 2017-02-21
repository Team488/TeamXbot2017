package telemetry;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InfluxDBConnection {

    protected final InfluxDB influxDB;
    protected final String dbName = "XbotRobotData";
    
    @Inject
    public InfluxDBConnection() {
        influxDB = InfluxDBFactory.connect("http://10.4.88.100:8086", "root", "root");
        
        // Flush every 2000 Points, at least every 1000ms
        influxDB.enableBatch(2000, 1000, TimeUnit.MILLISECONDS);
    }
    
    public void writePoint(Point point) {
        influxDB.write(dbName, "autogen", point);
    }
}
