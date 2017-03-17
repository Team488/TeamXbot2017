package competition.subsystems.pose;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {
    
    private DriveSubsystem drive;
    
    public static final double RED_ALLIANCE_HEADING_TO_FACE_HOPPER = 0.0;
    public static final double BLUE_ALLIANCE_HEADING_TO_FACE_HOPPER = 180.0;
    
    private final DoubleProperty distanceToWallFromBaseline;
        
    @Inject
    public PoseSubsystem(WPIFactory factory, XPropertyManager propManager, DriveSubsystem drive) {
        super(factory, propManager);
        this.drive = drive;
        
        distanceToWallFromBaseline = propManager.createPersistentProperty("Distance to wall from baseline", 96.0);
    }

    @Override
    protected double getLeftDriveDistance() {
        return drive.getLeftDistanceInInches();
    }

    @Override
    protected double getRightDriveDistance() {
        return drive.getRightDistanceInInches();
    }
    
    public Alliance getAllianceColor() {
        return DriverStation.getInstance().getAlliance();
    }
    
    public double getDistanceFromWallToBaseline() {
        return distanceToWallFromBaseline.get();
    }
    
}
