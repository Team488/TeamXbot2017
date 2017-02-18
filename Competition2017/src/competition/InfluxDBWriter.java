package competition;

import org.apache.log4j.Logger;

import com.db.influxdb.Configuration;
import com.db.influxdb.DataWriter;
import com.google.inject.Inject;

public class InfluxDBWriter {
    private static Logger log = Logger.getLogger(InfluxDBWriter.class);
   
    private DataWriter writer;
    
    @Inject
    public InfluxDBWriter() {
        Configuration config = new Configuration("172.22.11.1", "8086", "", "", "XbotRobotData");
        try {
            this.writer = new DataWriter(config);
            log.info("Created writer succesfully");
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    public void setMeasurementName(String measurementName) {
        writer.setMeasurement(measurementName);
    }
    
    public void writeData(String columnName, Object value) {
        writer.addField(columnName, value);
        try {
            writer.writeData();
        } catch (Exception e) {
            log.error(e);
        }
    }

}
