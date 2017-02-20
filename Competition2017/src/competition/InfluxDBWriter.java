package competition;

import java.io.IOException;
import java.time.temporal.IsoFields;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.db.influxdb.Configuration;
import com.db.influxdb.Constants;
import com.db.influxdb.DataWriter;
import com.google.inject.Inject;

public class InfluxDBWriter {
    private static Logger log = Logger.getLogger(InfluxDBWriter.class);
   
    private Configuration config;
    private DataWriter writer;
    private boolean dbOnline = false;
   
    @Inject
    public InfluxDBWriter() {
        config = new Configuration("10.4.88.100", "8086", "", "", "XbotRobotData");
        dbOnline = isInfluxOnline(config);
        try {
            this.writer = new DataWriter(config);
            log.info("Created writer succesfully");
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    public void setMeasurementName(String measurementName) {
        if (dbOnline) {
            writer.setMeasurement(measurementName);
        }
    }
    
    public void writeData(String columnName, Object value) {
        if (dbOnline) { 
            writer.addField(columnName, value);
            try {
                writer.writeData();
            }   catch (Exception e) {
                log.error(e);
            }
        }
    }
    
    private boolean isInfluxOnline(Configuration configuration) {
        URIBuilder uriBuilder = new URIBuilder();
        int port = Integer.parseInt(configuration.getPort());
        uriBuilder.setScheme(Constants.HTTP)
                .setHost(configuration.getHost())
                .setPort(port)
                .setPath(Constants.PING);
        CloseableHttpClient httpClient = null;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(1000)
                .setConnectTimeout(1000)
                .setConnectionRequestTimeout(1000)
                .build();
        try {
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .build();
            CloseableHttpResponse response = httpClient.execute(httpGet);
          
            Header[] headers = response.getHeaders("X-Influxdb-Version");
            if (headers == null || headers.length == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
} 
