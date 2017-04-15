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

    private final DoubleProperty redHeadingToBoiler;
    private final DoubleProperty blueHeadingToBoiler;

    private final DoubleProperty distanceToTravelToHopper;
    private final DoubleProperty distanceToReverseAfterHopper;
    
    protected final SetRobotHeadingCommand setInitialHeading;
    protected final DriveForDistanceAtHeadingCommand activateHopper;
    protected final DriveForDistanceAtHeadingCommand positionForShoot;
    protected final RotateToHeadingCommand rotateToBoiler;
    
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
        redAllianceStartingHeading =  propMan.createPersistentProperty("Red hopper and shoot starting heading", 90);
        blueAllianceStartingHeading = propMan.createPersistentProperty("Blue hopper and shoot starting heading", 90);
        
        redHeadingToHopper =  propMan.createPersistentProperty("Red heading to travel toward hopper", -110);
        blueHeadingToHopper = propMan.createPersistentProperty("Blue heading to travel toward hopper", -70);
        
        distanceToTravelToHopper = propMan.createPersistentProperty("Distance to activate hopper for load", -100);
        distanceToReverseAfterHopper = propMan.createPersistentProperty("Distance to reverse after hopper for shoot", -20);
        
        redHeadingToBoiler =  propMan.createPersistentProperty("Red heading to shoot at boiler", -40);
        blueHeadingToBoiler = propMan.createPersistentProperty("Blue heading to shoot at boiler", 220);
        
        shiftToHigh.setGear(Gear.HIGH_GEAR);
        addSequential(shiftToHigh, 0.1);

        this.setInitialHeading = setInitialHeading;
        setInitialHeading.setHeadingToApply(blueAllianceStartingHeading.get()); 
        addSequential(setInitialHeading);
        
        this.activateHopper = activateHopper;
        activateHopper.setTargetHeadingProp(blueHeadingToHopper);
        activateHopper.setTargetDistanceProp(distanceToTravelToHopper);
        addSequential(activateHopper);
        
        this.positionForShoot = positionForShoot;
        positionForShoot.setTargetHeadingProp(blueHeadingToHopper);
        positionForShoot.setTargetDistanceProp(distanceToReverseAfterHopper);
        addSequential(positionForShoot);
        
        this.rotateToBoiler = rotateToBoiler;
        rotateToBoiler.setTargetHeadingProp(blueHeadingToBoiler);
        addSequential(rotateToBoiler);
        
        shootLeft.setShooterRange(TypicalShootingPosition.OffsetFromHopper);
        addSequential(shootLeft);
    }

    public void setAlliance(Alliance color) {
        if (color == Alliance.Red) {
            setInitialHeading.setHeadingToApply(redAllianceStartingHeading.get());
            activateHopper.setTargetHeadingProp(redHeadingToHopper);
            positionForShoot.setTargetHeadingProp(redHeadingToHopper);
            rotateToBoiler.setTargetHeadingProp(redHeadingToBoiler);
        } else {
            setInitialHeading.setHeadingToApply(blueAllianceStartingHeading.get());
            activateHopper.setTargetHeadingProp(blueHeadingToHopper);
            positionForShoot.setTargetHeadingProp(blueHeadingToHopper);
            rotateToBoiler.setTargetHeadingProp(blueHeadingToBoiler);
        }
    }
}
