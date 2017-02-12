package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.properties.XPropertyManager;
import competition.subsystems.climbing.commands.AscendCommand;
import competition.subsystems.climbing.commands.DescendClimbingCommand;
import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.EjectAgitatorCommand;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.agitator.commands.StopAgitatorCommand;
import competition.subsystems.climbing.commands.RopeAlignerCommand;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.ResetDistanceCommand;

import competition.subsystems.drive.commands.TankDriveWithGamePadCommand;

import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shooter_belt.ShooterBeltSubsystem;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunBeltCommand;
import competition.subsystems.shooter_belt.commands.StopBeltCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.RunShooterCommand;
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
            StopShooterCommand stop) 
    {
        stop.setSide(shooterSubsystem.getLeftShooter());
 
        oi.leftButtons.getifAvailable(7).whenPressed(stop);
    }
    
    @Inject
    public void setupShooterBeltCommands(
            OperatorInterface oi,
            ShooterBeltsManagerSubsystem shooterBeltsSubsystem,
            RunBeltCommand run)
    {
        run.setShooterBeltSubsystem(shooterBeltsSubsystem.getLeftBelt());
        oi.rightButtons.getifAvailable(7).whileHeld(run);
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
            AgitatorsManagerSubsystem agitatorManagerSubsystem)
    {
        
        oi.controller.getXboxButton(XboxButtons.Y).whenPressed(new IntakeAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator()));
        oi.controller.getXboxButton(XboxButtons.A).whenPressed(new EjectAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator()));
        oi.controller.getXboxButton(XboxButtons.X).whenPressed(new StopAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator()));
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
}
