package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.commands.ResetDistanceCommand;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;
import xbot.common.controls.sensors.XXboxController.XboxButton;
import xbot.common.properties.DoubleProperty;

import competition.subsystems.climbing.commands.AscendClimbingCommand;
import competition.subsystems.climbing.commands.DescendClimbingCommand;
import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.EjectAgitatorCommand;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.autonomous.selection.DisableAutonomousCommand;
import competition.subsystems.autonomous.selection.SetupDriveToHopperThenBoilerCommand;
import competition.subsystems.climbing.commands.RopeAlignerCommand;
import competition.subsystems.collector.CollectorSubsystem.Power;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.DriveToPointUsingHeuristicsCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.drive.commands.TankDriveWithGamePadCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shoot_fuel.LeftShootFuelCommandGroup;
import competition.subsystems.shoot_fuel.RightShootFuelCommandGroup;
import competition.subsystems.shoot_fuel.ShootFuelCommandGroup;
import competition.subsystems.shooter_belt.ShooterBeltsManagerSubsystem;
import competition.subsystems.shooter_belt.commands.RunShooterBeltPowerCommand;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelUsingPowerCommand;

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
    
    @Inject
    public void setupClimbingCommands(
            OperatorInterface oi,
            AscendClimbingCommand ascend,
            RopeAlignerCommand aligner)   
    {
        oi.controller.getXboxButton(XboxButton.X).whileHeld(ascend);
        
        oi.controller.getXboxButton(XboxButton.Start).whileHeld(aligner);
        
        oi.operatorPanelButtons.getifAvailable(4).whileHeld(ascend);
    }

    @Inject
    public void setupShooterWheelCommands(
            OperatorInterface oi,
            ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem,
            LeftShootFuelCommandGroup shootLeft,
            RightShootFuelCommandGroup shootRight)
    {
        RunShooterWheelUsingPowerCommand runLeftPower = new RunShooterWheelUsingPowerCommand(
                shooterWheelsManagerSubsystem.getLeftShooter());
        RunShooterWheelUsingPowerCommand runRightPower = new RunShooterWheelUsingPowerCommand(
                shooterWheelsManagerSubsystem.getRightShooter());
        
        runLeftPower.includeOnSmartDashboard("Run shooter wheel using power - left");
        runRightPower.includeOnSmartDashboard("Run shooter wheel using power - right");
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
    
    @Inject
    public void setupCollectorCommands(
            OperatorInterface oi,
            EjectCollectorCommand eject,
            IntakeCollectorCommand intakeLowPower,
            IntakeCollectorCommand intakeHighPower
            )
    {
        intakeLowPower.setCollectorPower(Power.LOW);
        intakeHighPower.setCollectorPower(Power.HIGH);
        oi.controller.getXboxButton(XboxButton.A).whileHeld(intakeHighPower);
        oi.controller.getXboxButton(XboxButton.B).whileHeld(intakeLowPower);
        oi.controller.getXboxButton(XboxButton.Y).whileHeld(eject);
        
        oi.operatorPanelButtons.getifAvailable(2).whileHeld(intakeLowPower);
        oi.operatorPanelButtons.getifAvailable(3).whileHeld(eject);
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
        
        oi.controller.getXboxButton(XboxButton.LeftStick).whileHeld(ejectLeft);
        oi.controller.getXboxButton(XboxButton.RightStick).whileHeld(ejectRight);
        
        oi.operatorPanelButtons.getifAvailable(9).whileHeld(intakeLeft);
        oi.operatorPanelButtons.getifAvailable(8).whileHeld(ejectLeft);
        oi.operatorPanelButtons.getifAvailable(7).whileHeld(intakeRight);  
        oi.operatorPanelButtons.getifAvailable(6).whileHeld(ejectRight);  
    }
    
    @Inject
    public void setupDriveCommand(
            DriveForDistanceCommand driveForDistance, 
            ResetDistanceCommand resetDisplacement,
            RotateToHeadingCommand rotateToHeading,
            SetRobotHeadingCommand setHeading,
            XPropertyManager propManager,
            TankDriveWithGamePadCommand gamepad,
            DriveToPointUsingHeuristicsCommand driveUsingHeuristics)
    {
        DoubleProperty deltaDistance = propManager.createPersistentProperty("Drive for distance test distance", 20);
        resetDisplacement.includeOnSmartDashboard();
        driveForDistance.setDeltaDistance(deltaDistance);
        driveForDistance.includeOnSmartDashboard("Test drive for distance");
        gamepad.includeOnSmartDashboard("Change to GamePad Controls");
        
        setHeading.setHeadingToApply(90);
        rotateToHeading.setTargetHeading(180);
        
        setHeading.includeOnSmartDashboard();
        rotateToHeading.includeOnSmartDashboard();
        
        driveUsingHeuristics.setDeltaBasedTravel(60, 60, 90);
        driveUsingHeuristics.includeOnSmartDashboard();
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
    
    @Inject
    public void setupShooterCommandGroup(
            OperatorInterface oi,
            ShootFuelCommandGroup shootFuel,
            LeftShootFuelCommandGroup shootLeftFuel,
            RightShootFuelCommandGroup shootRightFuel,
            LeftShootFuelCommandGroup shootLeft,
            RightShootFuelCommandGroup shootRight)
    {

        oi.controller.getXboxButton(XboxButton.LeftTrigger).whileHeld(shootLeft);
        oi.controller.getXboxButton(XboxButton.RightTrigger).whileHeld(shootRight);
        
        oi.operatorPanelButtons.getifAvailable(1).whileHeld(shootFuel);
        oi.operatorPanelButtons.getifAvailable(10).whileHeld(shootLeftFuel);
        oi.operatorPanelButtons.getifAvailable(11).whileHeld(shootRightFuel);
    }
}