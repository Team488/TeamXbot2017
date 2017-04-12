package competition.subsystems.autonomous;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.DriveForDistanceAtHeadingCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shoot_fuel.LeftShootFuelAndAgitateCommandGroup;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;

public class ShootFromHopperCommandGroup extends CommandGroup {
    private final DoubleProperty redAllianceStartingHeading;
    private final DoubleProperty blueAllianceStartingHeading;

    private final DoubleProperty redHeadingToHopper;
    private final DoubleProperty blueHeadingToHopper;

    private final DoubleProperty distanceToTravelToHopper;
    
    protected final SetRobotHeadingCommand setInitialHeading;
    protected final DriveForDistanceAtHeadingCommand activateHopper;
    
    @Inject
    public ShootFromHopperCommandGroup(
            XPropertyManager propMan,
            SetRobotHeadingCommand setInitialHeading,
            ShiftGearCommand shiftToHigh,
            ShiftGearCommand shiftToLow,
            DriveForDistanceAtHeadingCommand activateHopper,
            DriveForDistanceAtHeadingCommand positionForShoot,
            RotateToHeadingCommand rotateToBoiler,
            LeftShootFuelAndAgitateCommandGroup shootLeft
            ) {
        redAllianceStartingHeading =  propMan.createPersistentProperty("Red shooting starting heading", -5);
        blueAllianceStartingHeading = propMan.createPersistentProperty("Blue shooting starting heading", -175);
        
        redHeadingToHopper =  propMan.createPersistentProperty("Red heading to travel toward hopper", -110);
        blueHeadingToHopper = propMan.createPersistentProperty("Blue heading to travel toward hopper", -70);
        
        distanceToTravelToHopper = propMan.createPersistentProperty("Distance to activate hopper for load", -100);
        
        shiftToHigh.setGear(Gear.HIGH_GEAR);
        addSequential(shiftToHigh, 0.1);

        this.setInitialHeading = setInitialHeading;
        setInitialHeading.setHeadingToApply(blueAllianceStartingHeading.get()); 
        addSequential(setInitialHeading);
        
        this.activateHopper = activateHopper;
        activateHopper.setTargetHeadingProp(blueHeadingToHopper);
        activateHopper.setTargetDistanceProp(distanceToTravelToHopper);
        addSequential(activateHopper);
        
        // TODO: Rotate to boiler
        
        shootLeft.setShooterRange(TypicalShootingPosition.OffsetFromHopper);
        addSequential(shootLeft);
    }

    public void setAlliance(Alliance color) {
        if (color == Alliance.Red) {
            setInitialHeading.setHeadingToApply(redAllianceStartingHeading.get());
            activateHopper.setTargetHeadingProp(redHeadingToHopper);
        } else {
            setInitialHeading.setHeadingToApply(blueAllianceStartingHeading.get());
            activateHopper.setTargetHeadingProp(blueHeadingToHopper);
        }
    }
}
