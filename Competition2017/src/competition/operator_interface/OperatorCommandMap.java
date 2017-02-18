package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.commands.ResetDistanceCommand;
import competition.subsystems.climbing.commands.AscendCommand;
import competition.subsystems.climbing.commands.DescendClimbingCommand;
import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.EjectAgitatorCommand;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.agitator.commands.StopAgitatorCommand;
import competition.subsystems.autonomous.selection.DisableAutonomousCommand;
import competition.subsystems.autonomous.selection.SetupDriveToHopperThenBoilerCommand;
import competition.subsystems.climbing.commands.RopeAlignerCommand;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.TankDriveWithGamePadCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunShooterBeltCommand;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import competition.subsystems.shooter_wheel.commands.StopShooterCommand;
import xbot.common.controls.sensors.XboxControllerWpiAdapter.XboxButton;
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
    public void setupShooterWheelCommands(
            OperatorInterface oi,
            ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem,
            XPropertyManager propertyManager) 
    {
        StopShooterCommand stopLeft = new StopShooterCommand(shooterWheelsManagerSubsystem.getLeftShooter());
        RunShooterWheelsForRangeCommand shootLeft = 
                new RunShooterWheelsForRangeCommand(
                        TypicalShootingPosition.FlushToBoiler, 
                        shooterWheelsManagerSubsystem.getLeftShooter());
        
        oi.leftButtons.getifAvailable(6).whenPressed(shootLeft);
        oi.leftButtons.getifAvailable(7).whenPressed(stopLeft);
    }
    
    @Inject
    public void setupShooterBeltCommands(
            OperatorInterface oi,
            ShooterBeltsManagerSubsystem shooterBeltsSubsystem)
    {
        RunShooterBeltCommand runBeltLeft = new RunShooterBeltCommand(shooterBeltsSubsystem.getLeftBelt());
        oi.rightButtons.getifAvailable(7).whileHeld(runBeltLeft);
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
        oi.controller.getXboxButton(XboxButton.LeftBumper).whileHeld(eject);
        oi.controller.getXboxButton(XboxButton.RightBumper).whileHeld(intake);
    }

    @Inject
    public void setupAgitatorCommands(
            OperatorInterface oi,
            AgitatorsManagerSubsystem agitatorManagerSubsystem)
    {
        
        oi.controller.getXboxButton(XboxButton.Y).whenPressed(new IntakeAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator()));
        oi.controller.getXboxButton(XboxButton.A).whenPressed(new EjectAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator()));
        oi.controller.getXboxButton(XboxButton.X).whenPressed(new StopAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator()));
    }
    
    // OTHER
    
    @Inject
    public void setupDriveCommand(
            DriveForDistanceCommand driveForDistance, 
            ResetDistanceCommand resetDisplacement,
            XPropertyManager propManager,
            TankDriveWithGamePadCommand gamepad
            )
    {
        DoubleProperty deltaDistance = propManager.createPersistentProperty("Drive for distance test distance", 20);
        resetDisplacement.includeOnSmartDashboard();
        driveForDistance.setDeltaDistance(deltaDistance);
        driveForDistance.includeOnSmartDashboard("Test drive for distance");
        gamepad.includeOnSmartDashboard("Change to GamePad Controls");
    }
    
    @Inject
    public void setupAutonomous(
            OperatorInterface oi,
            DisableAutonomousCommand disableCommand,
            SetupDriveToHopperThenBoilerCommand driveToBoiler
            ){
        disableCommand.includeOnSmartDashboard("Disable Autonomous");
        driveToBoiler.includeOnSmartDashboard("Run DriveToBoiler Autonomous Command");
    }
}
