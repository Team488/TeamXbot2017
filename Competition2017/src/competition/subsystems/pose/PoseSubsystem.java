package competition.subsystems.pose;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import xbot.common.controls.sensors.DistanceSensorPair;
import xbot.common.controls.sensors.MultiplexedLidarPair;
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
    private final DoubleProperty distanceBetweenDistanceSensorsProp;

    private final DoubleProperty breakBaselineMaxTime;
    private final DoubleProperty centerGearAutoMaxTime;

    private final DoubleProperty idealShootingRange;
    private final DoubleProperty leftLidarDistance;
    private final DoubleProperty rightLidarDistance;
    private final DoubleProperty distanceFromWallToCenterGear;
    protected final DistanceSensorPair frontLidars;

        
    @Inject
    public PoseSubsystem(WPIFactory factory, XPropertyManager propManager, DriveSubsystem drive) {
        super(factory, propManager);
        this.drive = drive;
        
        frontLidars = factory.getMultiplexedLidarPair(Port.kOnboard, (byte)0, (byte)1);
        
        distanceFromWallToCenterGear = propManager.createPersistentProperty("Distance from wall to center gear", 80);
        centerGearAutoMaxTime = propManager.createPersistentProperty("Center gear auto max time", 5.0);
        distanceToWallFromBaseline = propManager.createPersistentProperty("Distance to wall from baseline", 96.0);
        headingFacingBlueBoiler = propManager.createPersistentProperty("Heading facing blue boiler", -135);
        headingFacingRedBoiler = propManager.createPersistentProperty("Heading facing red boiler", -45);
        distanceBetweenDistanceSensorsProp = propManager.createPersistentProperty("Distance between distance sensors", 20);
        idealShootingRange = propManager.createPersistentProperty("Ideal shooting range", 18.0);
        leftLidarDistance = propManager.createEphemeralProperty("Left lidar distance", 0);      
        rightLidarDistance = propManager.createEphemeralProperty("Right lidar distance", 0);
    }

    @Override
    protected double getLeftDriveDistance() {
        return drive.getLeftDistanceInInches();
    }

    @Override
    protected double getRightDriveDistance() {
        return drive.getRightDistanceInInches();
    }
    
    public double getDistanceFromWallToCenterGear(){
        return distanceFromWallToCenterGear.get();
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
    
    public double getLidarFrontDistanceLeft() {
        return frontLidars.getSensorA().getDistance();
    }
    
    public double getLidarFrontDistanceRight() {
        return frontLidars.getSensorB().getDistance();
    }

    public double getLidarFrontDistanceAverage() {
        return (getLidarFrontDistanceLeft() + getLidarFrontDistanceRight()) / 2;
    }
    
    public double getLidarFrontAngle() {
        return Math.toDegrees(Math.atan(
                (getLidarFrontDistanceRight() - getLidarFrontDistanceLeft()) / distanceBetweenDistanceSensorsProp.get()));
    }
    
    public double getDistanceFromWallToBaseline() {
        return distanceToWallFromBaseline.get();
    }
    
    public double getBreakBaselineMaximumTime() {
        return breakBaselineMaxTime.get();
    }
    
    public double getCenterGearAutoMaxTime(){
        return centerGearAutoMaxTime.get();
    }
    
    public double getIdealShootingRange() {
        return idealShootingRange.get();
    }
    
    public DistanceSensorPair getLidar() {
        return frontLidars;
    }
    
    @Override
    public void updatePeriodicData() {
        super.updatePeriodicData();
        frontLidars.update();
        leftLidarDistance.set(frontLidars.getSensorA().getDistance());      
        rightLidarDistance.set(frontLidars.getSensorB().getDistance());
    }
    
}
