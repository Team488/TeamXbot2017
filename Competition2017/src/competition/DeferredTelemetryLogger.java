
package competition;

import xbot.common.command.PeriodicDataSource;
import xbot.common.properties.DoubleProperty;

public interface DeferredTelemetryLogger extends PeriodicDataSource {
    boolean isInitialized();
    void initialize(String name, DoubleProperty... properties);
    void collectNewDataset();
    void uninitialize();
}
