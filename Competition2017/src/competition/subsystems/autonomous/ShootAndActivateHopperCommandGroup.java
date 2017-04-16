package competition.subsystems.autonomous;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.subsystems.drive.commands.DriveForDistanceAtHeadingCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.DriveInfinitelyCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shoot_fuel.ShootFuelForNSecondsCommandGroup;
import competition.subsystems.shoot_fuel.StopFeedingAndCollectionCommandGroup;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;

public class ShootAndActivateHopperCommandGroup extends ShootAndDriveAcrossBaseLineCommandGroup {
    private RotateToHeadingCommand rotateToHopper;

    private final DoubleProperty redAllianceHeadingToHopper;
    private final DoubleProperty blueAllianceHeadingToHopper;
    private final DoubleProperty distanceFromShootingPositionToTurningPoint;

    @Inject
    public ShootAndActivateHopperCommandGroup(XPropertyManager propManager,
            SetRobotHeadingCommand setHeading,
            DriveForDistanceCommand driveBackABit,
            DriveForDistanceAtHeadingCommand driveAcrossBaseline,
            Provider<RotateToHeadingCommand> rotateToHeadingProvider,
            PoseSubsystem poseSubsystem,
            ShootFuelForNSecondsCommandGroup shootFuelCommandGroup,
            StopFeedingAndCollectionCommandGroup stopFiring,
            ShiftGearCommand shiftCommand) {
        super(propManager, setHeading, driveBackABit, driveAcrossBaseline, poseSubsystem, shootFuelCommandGroup, stopFiring, shiftCommand);
        
        redAllianceHeadingToHopper = propManager.createPersistentProperty("Red alliance heading to face hopper", 0);
        blueAllianceHeadingToHopper = propManager.createPersistentProperty("Blue alliance heading to face hopper", 180);
        distanceFromShootingPositionToTurningPoint = propManager.createPersistentProperty(
                "Vertical distance from boiler to hopper", 100);

        this.driveAcrossBaseline.setTargetDistanceProp(distanceFromShootingPositionToTurningPoint);
        
        rotateToHopper = rotateToHeadingProvider.get();
        rotateToHopper.setTargetHeadingProp(blueAllianceHeadingToHopper);
        this.addSequential(rotateToHopper, 2);
    }
    
    @Override
    public void setAlliance(Alliance color) {
        super.setAlliance(color);
        
        if(color == Alliance.Red) {
            rotateToHopper.setTargetHeadingProp(redAllianceHeadingToHopper);
        } else {
            rotateToHopper.setTargetHeadingProp(blueAllianceHeadingToHopper);
        }
    }
}
