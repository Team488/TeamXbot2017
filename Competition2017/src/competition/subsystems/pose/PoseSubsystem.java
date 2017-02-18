package competition.subsystems.pose;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.InfluxDBWriter;
import competition.subsystems.drive.DriveSubsystem;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {
    
    DriveSubsystem drive;
    private InfluxDBWriter influxWriter;
    @Inject
    public PoseSubsystem(WPIFactory factory, XPropertyManager propManager, DriveSubsystem drive, InfluxDBWriter influxWriter) {
        super(factory, propManager);
        this.drive = drive;
        this.influxWriter = influxWriter;
        this.influxWriter.setMeasurementName("PoseSubsystem");
        log.info("Creating");
    }

    @Override
    protected double getLeftDriveDistance() {
        return drive.getLeftDistanceInInches();
    }

    @Override
    protected double getRightDriveDistance() {
        return drive.getRightDistanceInInches();
    }

    @Override
    public void updatePeriodicData() {
        super.updatePeriodicData();
        influxWriter.writeData("CurrentHeading", this.getCurrentHeading().getValue());
    }
}
