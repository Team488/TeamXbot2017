package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.properties.XPropertyManager;
import competition.subsystems.climbing.commands.AscendCommand;
import competition.subsystems.climbing.commands.DescendClimbingCommand;
import competition.subsystems.agitator.commands.SpinAgitatorBackwardsCommand;
import competition.subsystems.agitator.commands.SpinAgitatorForwardsCommand;
import competition.subsystems.agitator.commands.StopAgitatorCommand;
import competition.subsystems.climbing.commands.RopeAlignerCommand;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.ResetDistanceCommand;
import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.SetShooterSpeedCommand;
import competition.subsystems.shooter_wheel.commands.StepShooterPowerCommand;
import competition.subsystems.shooter_wheel.commands.StopShooterCommand;
import xbot.common.controls.sensors.XboxControllerWpiAdapter.XboxButtons;
import xbot.common.properties.DoubleProperty;

@Singleton
public class OperatorCommandMap {
    // For mapping operator interface buttons to commands

    // Example for setting up a command to fire when a button is pressed:
    /*
    @Inject
    public void setupMyCommands(
            OperatorInterface operatorInterface,
            MyCommand myCommand)
    {
        operatorInterface.leftButtons.getifAvailable(1).whenPressed(myCommand);
    }
    */
    
    // JOYSTICK
    
    @Inject
    public void setupClimbingCommands(
            OperatorInterface oi,
            DescendClimbingCommand descend,
            AscendCommand ascend,
            RopeAlignerCommand aligner)   
    {
        oi.leftButtons.getifAvailable(2).whileHeld(descend);
        oi.rightButtons.getifAvailable(2).whileHeld(ascend);
        oi.rightButtons.getifAvailable(5).whileHeld(aligner);
    }
    
    @Inject
    public void setupLauncherCommands(
            OperatorInterface oi,
            ShooterWheelsManagerSubsystem shooterSubsystem,
            XPropertyManager propertyManager,
            StepShooterPowerCommand stepPower,
            StopShooterCommand stop,
            SetShooterSpeedCommand setShooter
            ) {
        stepPower.setSide(shooterSubsystem.getLeftShooter());
        stop.setSide(shooterSubsystem.getLeftShooter());
        setShooter.setSide(shooterSubsystem.getLeftShooter());
 
        oi.leftButtons.getifAvailable(6).whenPressed(stepPower);
        oi.leftButtons.getifAvailable(7).whenPressed(stop);
        oi.leftButtons.getifAvailable(8).whenPressed(setShooter);
    }
    
   @Inject
   public void setupShiftCommand(
           OperatorInterface oi,
           ShiftGearCommand shiftLow, 
           ShiftGearCommand shiftHigh)
   {
       shiftLow.setGear(Gear.LOW_GEAR);
       oi.leftButtons.getifAvailable(1).whenPressed(shiftLow);
       shiftHigh.setGear(Gear.HIGH_GEAR);
       oi.rightButtons.getifAvailable(1).whenPressed(shiftHigh);
   }
    
    // CONTROLLER
    
    @Inject
    public void setupCollectorCommands(
            OperatorInterface oi,
            EjectCollectorCommand eject,
            IntakeCollectorCommand intake)
    {
        oi.controller.getXboxButton(XboxButtons.LeftBumper).whileHeld(eject);
        oi.controller.getXboxButton(XboxButtons.RightBumper).whileHeld(intake);
    }

    @Inject
    public void setupAgitatorCommands(
            OperatorInterface oi,
            SpinAgitatorForwardsCommand forwards,
            SpinAgitatorBackwardsCommand backwards,
            StopAgitatorCommand stop)
    {
        oi.controller.getXboxButton(XboxButtons.Y).whenPressed(forwards);
        oi.controller.getXboxButton(XboxButtons.A).whenPressed(backwards);
        oi.controller.getXboxButton(XboxButtons.X).whenPressed(stop);
    }
    
    // OTHER
    
    @Inject
    public void setupDriveCommand(
            DriveForDistanceCommand driveForDistance, 
            ResetDistanceCommand resetDisplacement,
            XPropertyManager propManager)
    {
        DoubleProperty deltaDistance = propManager.createPersistentProperty("Drive for distance test distance", 20);
        resetDisplacement.includeOnSmartDashboard();
        driveForDistance.setDeltaDistance(deltaDistance);
        driveForDistance.includeOnSmartDashboard("Test drive for distance");
    }
}
