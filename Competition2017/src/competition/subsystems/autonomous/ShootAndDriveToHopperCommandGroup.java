package competition.subsystems.autonomous;

import com.google.inject.Provider;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShootAndDriveToHopperCommandGroup extends CommandGroup {
    private ShootAndDriveAcrossBaseLineCommandGroup shootAndBaseLine;
    private DriveForDistanceCommand driveToHopper;
    private RotateToHeadingCommand rotateToHopper;

    private final DoubleProperty redAllianceHeadingToHopper;
    private final DoubleProperty blueAllianceHeadingToHopper;
    private final DoubleProperty distanceToHopperFromTurningPoint;
    
    private Alliance color;

    public ShootAndDriveToHopperCommandGroup(XPropertyManager propManager,
            ShootAndDriveAcrossBaseLineCommandGroup shootAndBaseLine,
            Provider<DriveForDistanceCommand> driveForDistanceProvider,
            Provider<RotateToHeadingCommand> rotateToHopperProvider) {
        redAllianceHeadingToHopper = propManager.createPersistentProperty(" red alliance heading to face hopper", 90);
        blueAllianceHeadingToHopper = propManager.createPersistentProperty(" blue alliance heading to face hopper",
                180);
        distanceToHopperFromTurningPoint = propManager.createPersistentProperty(
                " distance from the turning point behind baseline to hopper in inches", 43.25);

        this.shootAndBaseLine = shootAndBaseLine;
        this.shootAndBaseLine.setAlliance(color);
        this.addSequential(shootAndBaseLine);
        
        rotateToHopper = rotateToHopperProvider.get();
        if(color == Alliance.Red) {
            rotateToHopper.setTargetHeading(redAllianceHeadingToHopper.get());
        } else if (color == Alliance.Blue){
            rotateToHopper.setTargetHeading(blueAllianceHeadingToHopper.get());
        }
        this.addSequential(rotateToHopper);
        
        driveToHopper = driveForDistanceProvider.get();
        driveToHopper.setDeltaDistance(distanceToHopperFromTurningPoint);
        this.addParallel(driveToHopper);
    }
    
    public void setAllianceForShootAndHopperAuto(Alliance color) {
       this.color = color;
    }

}
