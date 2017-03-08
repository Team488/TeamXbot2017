package competition;

import xbot.common.properties.DoubleProperty;

public class MockDeferredTelemetryLogger implements DeferredTelemetryLogger {

    @Override
    public void updatePeriodicData() {
        // Intentionally left blank
    }

    @Override
    public void initialize(String name, DoubleProperty... properties) {
        // Intentionally left blank
    }

    @Override
    public void collectNewDataset() {
        // Intentionally left blank
    }

    @Override
    public void uninitialize() {
        // Intentionally left blank
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

}
