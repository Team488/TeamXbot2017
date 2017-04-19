package competition.subsystems.autonomous;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.DriveForDistanceAtHeadingCommand;
import competition.subsystems.drive.commands.DriveInfinitelyCommand;
import competition.subsystems.drive.commands.StopDriveCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shoot_fuel.EndToEndShootingCommandGroup;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import competition.subsystems.shooter_wheel.commands.StaggerStartBothShootersCommandGroup;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import xbot.common.command.BaseCommandGroup;
import xbot.common.command.TimeoutCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;

public class ShootFromHopperCommandGroup extends BaseCommandGroup {
    private final DoubleProperty redAllianceStartingHeading;
    private final DoubleProperty blueAllianceStartingHeading;

    private final DoubleProperty redHeadingToHopper;
    private final DoubleProperty blueHeadingToHopper;

    private final DoubleProperty redHeadingToBoiler;
    private final DoubleProperty blueHeadingToBoiler;

    private final DoubleProperty distanceToTravelToHopper;
    private final DoubleProperty distanceToReverseAfterHopper;

    private final DoubleProperty shiftWaitTime;
    private final DoubleProperty rampDistance;
    private final DoubleProperty nudgePower;
    private final DoubleProperty nudgeTime;
    
    protected final SetRobotHeadingCommand setInitialHeading;
    protected final DriveForDistanceAtHeadingCommand activateHopper;
    protected final DriveForDistanceAtHeadingCommand positionForShoot;
    
    @Inject
    public ShootFromHopperCommandGroup(
            XPropertyManager propMan,
            SetRobotHeadingCommand setInitialHeading,
            DriveInfinitelyCommand nudgeIntoGear,
            StaggerStartBothShootersCommandGroup startShooters,
            ShiftGearCommand shiftToHigh,
            ShiftGearCommand shiftToLow,
            DriveForDistanceAtHeadingCommand activateHopper,
            DriveForDistanceAtHeadingCommand positionForShoot,
            StopDriveCommand stopDrive,
            EndToEndShootingCommandGroup shootBoth
            ) {
        redAllianceStartingHeading =  propMan.createPersistentProperty("Red hopper and shoot starting heading", -90);
        blueAllianceStartingHeading = propMan.createPersistentProperty("Blue hopper and shoot starting heading", -90);
        
        redHeadingToHopper =  propMan.createPersistentProperty("Red heading to travel toward hopper", -108);
        blueHeadingToHopper = propMan.createPersistentProperty("Blue heading to travel toward hopper", -72);
        
        distanceToTravelToHopper = propMan.createPersistentProperty("Distance to activate hopper for load", -95);
        distanceToReverseAfterHopper = propMan.createPersistentProperty("Distance to reverse after hopper for shoot", 23);
        
        
        redHeadingToBoiler =  propMan.createPersistentProperty("Red heading to shoot at boiler", -78);
        blueHeadingToBoiler = propMan.createPersistentProperty("Blue heading to shoot at boiler", -102);

        shiftWaitTime = propMan.createPersistentProperty("Wait time after shift", 0.1);
        rampDistance = propMan.createPersistentProperty("Ramp distance in auto", -1);
        nudgePower = propMan.createPersistentProperty("Nudge into gear power", -0.3);
        nudgeTime = propMan.createPersistentProperty("Nudge into gear time", 0.1);

        
        shiftToHigh.setGear(Gear.HIGH_GEAR);
        addSequential(shiftToHigh, 0.1);
        
        this.setInitialHeading = setInitialHeading;
        setInitialHeading.setHeadingToApply(blueAllianceStartingHeading.get()); 
        addSequential(setInitialHeading);
        
        nudgeIntoGear.setDrivePowerProp(nudgePower);
        addSequential(nudgeIntoGear, nudgeTime.get());
        
        //addSequential(new TimeoutCommand(shiftWaitTime));

        startShooters.setTargetRange(TypicalShootingPosition.OffsetFromHopper);
        addParallel(startShooters);
        
        this.activateHopper = activateHopper;
        activateHopper.setTargetHeadingProp(blueHeadingToHopper);
        activateHopper.setTargetDistanceProp(distanceToTravelToHopper);
        //activateHopper.setRampDistanceProp(rampDistance);
        addSequential(activateHopper);

        shiftToLow.setGear(Gear.LOW_GEAR);
        addSequential(shiftToLow, 0.1);

        //addSequential(new TimeoutCommand(shiftWaitTime));
        
        this.positionForShoot = positionForShoot;
        positionForShoot.setTargetHeadingProp(blueHeadingToBoiler);
        positionForShoot.setTargetDistanceProp(distanceToReverseAfterHopper);
        addSequential(positionForShoot);
        addSequential(stopDrive);
        
        shootBoth.setShooterRange(TypicalShootingPosition.OffsetFromHopper);
        addSequential(shootBoth);
    }

    public void setAlliance(Alliance color) {
        if (color == Alliance.Red) {
            setInitialHeading.setHeadingToApply(redAllianceStartingHeading.get());
            activateHopper.setTargetHeadingProp(redHeadingToHopper);
            positionForShoot.setTargetHeadingProp(redHeadingToBoiler);
        } else {
            setInitialHeading.setHeadingToApply(blueAllianceStartingHeading.get());
            activateHopper.setTargetHeadingProp(blueHeadingToHopper);
            positionForShoot.setTargetHeadingProp(blueHeadingToBoiler);
        }
    }
}
