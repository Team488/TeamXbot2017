package competition.subsystems.autonomous;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.subsystems.collector.commands.StopCollectorCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shoot_fuel.ShootFuelForNSecondsCommandGroup;
import competition.subsystems.shoot_fuel.StopAllShootingCommandGroup;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;

public class ShootAndDriveAcrossBaseLineCommandGroup extends CommandGroup{
    
    private final DoubleProperty distanceToBackUp;
    
    private final DoubleProperty redAllianceStartingHeading;
    private final DoubleProperty blueAllianceStartingHeading;
    
    private SetRobotHeadingCommand setInitialHeading;
    private ShootFuelForNSecondsCommandGroup shootFuel;
    private DriveForDistanceCommand breakBaselineAuto;
    private DriveForDistanceCommand driveBackABit;
    
    @Inject
    public ShootAndDriveAcrossBaseLineCommandGroup(XPropertyManager propManager,
        SetRobotHeadingCommand setHeading,
        Provider <DriveForDistanceCommand> driveForDistanceProvider,
        RotateToHeadingCommand rotateToBaseline,
        PoseSubsystem poseSubsystem,
        ShootFuelForNSecondsCommandGroup shootFuelCommandGroup,
        StopAllShootingCommandGroup stopFiring,
        ShiftGearCommand shiftCommand) {
        
        redAllianceStartingHeading =  propManager.createPersistentProperty("Red shooting starting heading", -5);
        blueAllianceStartingHeading = propManager.createPersistentProperty("Blue shooting starting heading", -175);
        
        shiftCommand.setGear(Gear.HIGH_GEAR);
        this.addSequential(shiftCommand, 0.1);
        
        this.setInitialHeading = setHeading;
        this.addSequential(setInitialHeading);
        // Default to Red alliance, this can be changed later in setAlliance()
        this.setInitialHeading.setHeadingToApply(redAllianceStartingHeading.get());        
        
        distanceToBackUp = propManager.createPersistentProperty("Reverse distance for shoot then baseline", -6);
        
        shootFuel = shootFuelCommandGroup;
        this.addSequential(shootFuel);
        
        driveBackABit = driveForDistanceProvider.get();
        driveBackABit.setDeltaDistance(distanceToBackUp);
        
        this.addSequential(driveBackABit, 0.25);
        this.addParallel(stopFiring, 0.25);
        
        //aim away from driver station (towards baseline)
        rotateToBaseline.setTargetHeading(90);
        this.addSequential(rotateToBaseline, 1.2);
        
        // Maybe here we change it to high gear?
        breakBaselineAuto = driveForDistanceProvider.get();
        breakBaselineAuto.setDeltaDistance(poseSubsystem.getDistanceFromWallToBaseline());
        this.addSequential(breakBaselineAuto, poseSubsystem.getBreakBaselineMaximumTime());
    }
    
    public void setAlliance(Alliance color) {
        if (color == Alliance.Red) {
            setInitialHeading.setHeadingToApply(redAllianceStartingHeading.get());
        } else if (color == Alliance.Blue) {
            setInitialHeading.setHeadingToApply(blueAllianceStartingHeading.get());
        }
    }
}