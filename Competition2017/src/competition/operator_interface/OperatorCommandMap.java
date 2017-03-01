package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.commands.ResetDistanceCommand;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;
import xbot.common.controls.sensors.XXboxController.XboxButton;
import xbot.common.properties.DoubleProperty;

import competition.subsystems.climbing.commands.AscendCommand;
import competition.subsystems.climbing.commands.DescendClimbingCommand;
import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.EjectAgitatorCommand;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.autonomous.selection.DisableAutonomousCommand;
import competition.subsystems.autonomous.selection.SetupDriveToHopperThenBoilerCommand;
import competition.subsystems.climbing.commands.RopeAlignerCommand;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.drive.commands.TankDriveWithGamePadCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunShooterBeltPowerCommand;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelUsingPowerCommand;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import competition.subsystems.shooter_wheel.commands.StopShooterCommand;

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
        oi.leftButtons.getifAvailable(3).whileHeld(descend);
        oi.rightButtons.getifAvailable(3).whileHeld(ascend);
        
        oi.rightButtons.getifAvailable(8).whileHeld(aligner);
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
        
        StopShooterCommand stopRight = new StopShooterCommand(shooterWheelsManagerSubsystem.getRightShooter());
        RunShooterWheelsForRangeCommand shootRight = 
                new RunShooterWheelsForRangeCommand(
                        TypicalShootingPosition.FlushToBoiler, 
                        shooterWheelsManagerSubsystem.getRightShooter());
        
        RunShooterWheelUsingPowerCommand runLeftPower = new RunShooterWheelUsingPowerCommand(
                shooterWheelsManagerSubsystem.getLeftShooter());
        RunShooterWheelUsingPowerCommand runRightPower = new RunShooterWheelUsingPowerCommand(
                shooterWheelsManagerSubsystem.getRightShooter());
        
        oi.controller.getXboxButton(XboxButton.LeftTrigger).whileHeld(runLeftPower);
        oi.controller.getXboxButton(XboxButton.RightTrigger).whileHeld(runRightPower);
        
        oi.leftButtons.getifAvailable(5).whenPressed(shootLeft);
        oi.leftButtons.getifAvailable(4).whenPressed(stopLeft);
        oi.rightButtons.getifAvailable(4).whenPressed(shootRight);
        oi.rightButtons.getifAvailable(5).whenPressed(stopRight);
    }
    
    @Inject
    public void setupShooterBeltCommands(
            OperatorInterface oi,
            ShooterBeltsManagerSubsystem shooterBeltsSubsystem)
    {
        RunShooterBeltPowerCommand runBeltLeft = new RunShooterBeltPowerCommand(shooterBeltsSubsystem.getLeftBelt());
        oi.leftButtons.getifAvailable(2).whileHeld(runBeltLeft);
        RunShooterBeltPowerCommand runBeltRight = new RunShooterBeltPowerCommand(shooterBeltsSubsystem.getRightBelt());
        oi.rightButtons.getifAvailable(2).whileHeld(runBeltRight);
    }
    
   @Inject
   public void setupShiftCommand(
           OperatorInterface oi,
           ShiftGearCommand shiftLow, 
           ShiftGearCommand shiftHigh)
   {
       shiftLow.setGear(Gear.LOW_GEAR);
       shiftHigh.setGear(Gear.HIGH_GEAR);
       shiftLow.includeOnSmartDashboard("Shift low");
       shiftHigh.includeOnSmartDashboard("Shift high");
       
       oi.leftButtons.getifAvailable(1).whenPressed(shiftHigh);
       oi.rightButtons.getifAvailable(1).whenPressed(shiftLow);
   }
    
    // CONTROLLER
    
    @Inject
    public void setupCollectorCommands(
            OperatorInterface oi,
            EjectCollectorCommand eject,
            IntakeCollectorCommand intake)
    {
        oi.controller.getXboxButton(XboxButton.A).whileHeld(intake);
        oi.controller.getXboxButton(XboxButton.B).whileHeld(eject);
    }

    @Inject
    public void setupAgitatorCommands(
            OperatorInterface oi,
            AgitatorsManagerSubsystem agitatorManagerSubsystem)
    {
        IntakeAgitatorCommand intakeLeft = new IntakeAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator());
        IntakeAgitatorCommand intakeRight = new IntakeAgitatorCommand(agitatorManagerSubsystem.getRightAgitator());
        EjectAgitatorCommand ejectLeft = new EjectAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator());
        EjectAgitatorCommand ejectRight = new EjectAgitatorCommand(agitatorManagerSubsystem.getRightAgitator());
        
        intakeLeft.includeOnSmartDashboard("Left Agitator Intake");
        intakeRight.includeOnSmartDashboard("Right Agitator Intake");
        
        ejectLeft.includeOnSmartDashboard("Left Agitator Eject");
        ejectRight.includeOnSmartDashboard("Right Agitator Eject");
        
        oi.controller.getXboxButton(XboxButton.LeftBumper).whileHeld(intakeLeft);
        oi.controller.getXboxButton(XboxButton.RightBumper).whileHeld(intakeRight);
        
    }
    // OTHER
    
    @Inject
    public void setupDriveCommand(
            DriveForDistanceCommand driveForDistance, 
            ResetDistanceCommand resetDisplacement,
            RotateToHeadingCommand rotateToHeading,
            SetRobotHeadingCommand setHeading,
            XPropertyManager propManager,
            TankDriveWithGamePadCommand gamepad)
    {
        DoubleProperty deltaDistance = propManager.createPersistentProperty("Drive for distance test distance", 20);
        resetDisplacement.includeOnSmartDashboard();
        driveForDistance.setDeltaDistance(deltaDistance);
        driveForDistance.includeOnSmartDashboard("Test drive for distance");
        gamepad.includeOnSmartDashboard("Change to GamePad Controls");
        
        setHeading.setHeadingToApply(0);
        rotateToHeading.setTargetHeading(90);
        
        setHeading.includeOnSmartDashboard();
        rotateToHeading.includeOnSmartDashboard();
    }
    
    @Inject
    public void setupAutonomous(
            OperatorInterface oi,
            DisableAutonomousCommand disableCommand,
            SetupDriveToHopperThenBoilerCommand driveToBoiler)
    {
        disableCommand.includeOnSmartDashboard("Disable Autonomous");
        driveToBoiler.includeOnSmartDashboard("Run DriveToBoiler Autonomous Command");
    }
}
