package competition.subsystems.autonomous;

import com.google.inject.Provider;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shoot_fuel.ShootFuelForNSecondsCommandGroup;
import competition.subsystems.shoot_fuel.StopAllShootingCommandGroup;
import competition.subsystems.shoot_fuel.StopFeedingAndCollectionCommandGroup;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;

public class ShootAndActivateHopperCommandGroup extends ShootAndDriveAcrossBaseLineCommandGroup {
    private ShootAndDriveAcrossBaseLineCommandGroup shootAndBaseLine;
    private DriveForDistanceCommand driveToHopper;
    private RotateToHeadingCommand rotateToHopper;

    private final DoubleProperty redAllianceHeadingToHopper;
    private final DoubleProperty blueAllianceHeadingToHopper;
    private final DoubleProperty distanceToHopperFromTurningPoint;
    private final DoubleProperty distanceFromShootingPositionToTurningPoint;

    public ShootAndActivateHopperCommandGroup(XPropertyManager propManager,
            SetRobotHeadingCommand setHeading,
            Provider <DriveForDistanceCommand> driveForDistanceProvider,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider,
            PoseSubsystem poseSubsystem,
            ShootFuelForNSecondsCommandGroup shootFuelCommandGroup,
            StopFeedingAndCollectionCommandGroup stopFiring,
            ShiftGearCommand shiftCommand) {
        super(propManager, setHeading, driveForDistanceProvider, rotateToHeadingProvider.get(), poseSubsystem, shootFuelCommandGroup, stopFiring, shiftCommand);
        
        redAllianceHeadingToHopper = propManager.createPersistentProperty("Red alliance heading to face hopper", 90);
        blueAllianceHeadingToHopper = propManager.createPersistentProperty("Blue alliance heading to face hopper", 180);
        distanceToHopperFromTurningPoint = propManager.createPersistentProperty(
                "Lateral distance from shoot position to hopper in inches", 43.25);
        distanceFromShootingPositionToTurningPoint = propManager.createPersistentProperty(
                "Vertical distance from boiler to hopper", 43.25);

        this.driveAcrossBaseline.setDeltaDistance(distanceFromShootingPositionToTurningPoint.get());
        
        rotateToHopper = rotateToHeadingProvider.get();
        rotateToHopper.setTargetHeading(blueAllianceHeadingToHopper.get());
        this.addSequential(rotateToHopper);
        
        driveToHopper = driveForDistanceProvider.get();
        driveToHopper.setDeltaDistance(distanceToHopperFromTurningPoint);
        this.addSequential(driveToHopper);
    }
    
    @Override
    public void setAlliance(Alliance color) {
        super.setAlliance(color);
        
        if(color == Alliance.Red) {
            rotateToHopper.setTargetHeading(redAllianceHeadingToHopper.get());
        } else {
            rotateToHopper.setTargetHeading(blueAllianceHeadingToHopper.get());
        }
    }
}
