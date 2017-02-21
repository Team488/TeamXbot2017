package competition.subsystems.pose;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {
    
    DriveSubsystem drive;
    
    @Inject
    public PoseSubsystem(WPIFactory factory, XPropertyManager propManager, DriveSubsystem drive) {
        super(factory, propManager);
        this.drive = drive;
    }

    @Override
    protected double getLeftDriveDistance() {
        return drive.getLeftDistanceInInches();
    }

    @Override
    protected double getRightDriveDistance() {
        return drive.getRightDistanceInInches();
    }

}
