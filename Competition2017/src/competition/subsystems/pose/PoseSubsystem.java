package competition.subsystems.pose;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.ContiguousHeading;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {
    
    private DriveSubsystem drive;
    
    public static final double RED_ALLIANCE_HEADING_TO_FACE_HOPPER = 0.0;
    public static final double BLUE_ALLIANCE_HEADING_TO_FACE_HOPPER = 180.0;
    
    private final DoubleProperty distanceToWallFromBaseline;
    private final DoubleProperty headingFacingBlueBoiler;
    private final DoubleProperty headingFacingRedBoiler;
    private final DoubleProperty breakBaselineMaxTime;
        
    @Inject
    public PoseSubsystem(WPIFactory factory, XPropertyManager propManager, DriveSubsystem drive) {
        super(factory, propManager);
        this.drive = drive;
        
        distanceToWallFromBaseline = propManager.createPersistentProperty("Distance to wall from baseline", 96.0);
        headingFacingBlueBoiler = propManager.createPersistentProperty("Heading facing blue boiler", -135);
        headingFacingRedBoiler = propManager.createPersistentProperty("Heading facing red boiler", -45);
        
        breakBaselineMaxTime = propManager.createPersistentProperty("Break baseline maximum time", 3.0);
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
    
    public ContiguousHeading getHeadingFacingBoiler() {
        Alliance allianceColor = getAllianceColor();
        
        if(allianceColor == Alliance.Blue) {
            return new ContiguousHeading(headingFacingBlueBoiler.get());
        }
        else { // Red is the default alliance if data is unavailable
            return new ContiguousHeading(headingFacingRedBoiler.get());
        }
    }
    
    public double getDistanceFromWallToBaseline() {
        return distanceToWallFromBaseline.get();
    }
    
    public double getBreakBaselineMaximumTime() {
        return breakBaselineMaxTime.get();
    }
    
}
