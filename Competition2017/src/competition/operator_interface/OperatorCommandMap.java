package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.commands.ResetDistanceCommand;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;
import xbot.common.controls.sensors.XXboxController.XboxButton;
import xbot.common.math.PIDFactory;
import xbot.common.properties.DoubleProperty;

import competition.subsystems.climbing.commands.AscendClimbingCommand;
import competition.subsystems.agitator.AgitatorsManagerSubsystem;
import competition.subsystems.agitator.commands.EjectAgitatorCommand;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import competition.subsystems.autonomous.DriveToBoilerUsingHeuristicsWithVisionCommandGroup;
import competition.subsystems.autonomous.selection.DisableAutonomousCommand;
import competition.subsystems.autonomous.selection.SetupBreakBaselineCommand;
import competition.subsystems.autonomous.selection.SetupDriveToHopperThenBoilerCommand;
import competition.subsystems.autonomous.selection.SetupShootAndDriveAcrossBaseLineCommand;
import competition.subsystems.climbing.commands.RopeAlignerCommand;
import competition.subsystems.collector.CollectorSubsystem.Power;
import competition.subsystems.collector.commands.EjectCollectorCommand;
import competition.subsystems.collector.commands.IntakeCollectorCommand;
import competition.subsystems.vision.commands.DriveTowardBoilerWithVisionAndJoysticksCommand;
import competition.subsystems.vision.commands.RotateRobotToBoilerCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.DriveToPointUsingHeuristicsCommand;
import competition.subsystems.drive.commands.DriveToShootingRangeCommand;
import competition.subsystems.drive.commands.RotateToHeadingCommand;
import competition.subsystems.drive.commands.TankDriveWithGamePadCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import competition.subsystems.shoot_fuel.LeftFeedingCommandGroup;
import competition.subsystems.shoot_fuel.LeftShootFuelCommandGroup;
import competition.subsystems.shoot_fuel.RightFeedingCommandGroup;
import competition.subsystems.shoot_fuel.RightShootFuelCommandGroup;
import competition.subsystems.shoot_fuel.ShootFuelCommandGroup;
import competition.subsystems.shoot_fuel.UnjamLeftCommandGroup;
import competition.subsystems.shoot_fuel.UnjamRightCommandGroup;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelUsingPowerCommand;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeCommand;
import competition.subsystems.shooter_wheel.commands.RunShooterWheelsForRangeVirtualThrottleCommand;
import competition.subsystems.shooter_wheel.commands.TrimShooterWheelCommand;
import competition.subsystems.shooter_wheel.commands.TrimShooterWheelCommand.TrimDirection;

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
        
        //oi.operatorPanelButtons.getifAvailable(4).whileHeld(ascend);
    }

    @Inject
    public void setupShooterWheelCommands(
            OperatorInterface oi,
            ShooterWheelsManagerSubsystem shooterWheelsManagerSubsystem,
            LeftShootFuelCommandGroup shootLeft,
            RightShootFuelCommandGroup shootRight,
            XPropertyManager propMan,
            PIDFactory pidFactory
            )
    {
        ShooterWheelSubsystem leftWheel = shooterWheelsManagerSubsystem.getLeftShooter();
        ShooterWheelSubsystem rightWheel = shooterWheelsManagerSubsystem.getRightShooter();
        
        RunShooterWheelUsingPowerCommand runLeftPower = new RunShooterWheelUsingPowerCommand(
                leftWheel);
        RunShooterWheelUsingPowerCommand runRightPower = new RunShooterWheelUsingPowerCommand(
                rightWheel);
        
        TrimShooterWheelCommand leftUp = new TrimShooterWheelCommand(leftWheel, propMan);
        leftUp.setTrimDirection(TrimDirection.Up);
        TrimShooterWheelCommand rightUp = new TrimShooterWheelCommand(rightWheel, propMan);
        rightUp.setTrimDirection(TrimDirection.Up);
        
        TrimShooterWheelCommand leftDown = new TrimShooterWheelCommand(leftWheel, propMan);
        leftUp.setTrimDirection(TrimDirection.Down);
        TrimShooterWheelCommand rightDown = new TrimShooterWheelCommand(rightWheel, propMan);
        rightUp.setTrimDirection(TrimDirection.Down);
        
        RunShooterWheelsForRangeCommand runLeftWheel = 
                new RunShooterWheelsForRangeCommand(
                        TypicalShootingPosition.FlushToBoiler,
                        shooterWheelsManagerSubsystem.getLeftShooter());
        
        RunShooterWheelsForRangeCommand runRightWheel = 
                new RunShooterWheelsForRangeCommand(
                        TypicalShootingPosition.FlushToBoiler,
                        shooterWheelsManagerSubsystem.getRightShooter());
        
        oi.controller.getXboxButton(XboxButton.LeftTrigger).whileHeld(runLeftWheel);
        oi.controller.getXboxButton(XboxButton.RightTrigger).whileHeld(runRightWheel);
        
        runLeftPower.includeOnSmartDashboard("Run shooter wheel using power - left");
        runRightPower.includeOnSmartDashboard("Run shooter wheel using power - right");
        
        oi.operatorPanelButtons.getIfAvailable(9).whenPressed(leftUp);
        oi.operatorPanelButtons.getIfAvailable(8).whenPressed(leftDown);
        
        oi.operatorPanelButtons.getIfAvailable(7).whenPressed(rightUp);
        oi.operatorPanelButtons.getIfAvailable(6).whenPressed(rightDown);
        
        RunShooterWheelsForRangeVirtualThrottleCommand runVirtualThrottleLeft = new RunShooterWheelsForRangeVirtualThrottleCommand(
                TypicalShootingPosition.FlushToBoiler,
                shooterWheelsManagerSubsystem.getLeftShooter(),
                pidFactory
                );
        RunShooterWheelsForRangeVirtualThrottleCommand runVirtualThrottleRight = new RunShooterWheelsForRangeVirtualThrottleCommand(
                TypicalShootingPosition.FlushToBoiler,
                shooterWheelsManagerSubsystem.getRightShooter(),
                pidFactory
                );
        runVirtualThrottleLeft.includeOnSmartDashboard("Run shooter wheel using virtual throttle - left");
        runVirtualThrottleRight.includeOnSmartDashboard("Run shooter wheel using virtual throttle - right");
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
       
       oi.leftButtons.getIfAvailable(1).whenPressed(shiftHigh);
       oi.rightButtons.getIfAvailable(1).whenPressed(shiftLow);
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
        
        //oi.operatorPanelButtons.getifAvailable(2).whileHeld(intakeLowPower);
        //oi.operatorPanelButtons.getifAvailable(3).whileHeld(eject);
    }

    @Inject
    public void setupAgitatorCommands(
            OperatorInterface oi,
            AgitatorsManagerSubsystem agitatorManagerSubsystem,
            UnjamLeftCommandGroup unjamLeft,
            UnjamRightCommandGroup unjamRight)
    {
        IntakeAgitatorCommand intakeLeft = new IntakeAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator());
        IntakeAgitatorCommand intakeRight = new IntakeAgitatorCommand(agitatorManagerSubsystem.getRightAgitator());
        EjectAgitatorCommand ejectLeft = new EjectAgitatorCommand(agitatorManagerSubsystem.getLeftAgitator());
        EjectAgitatorCommand ejectRight = new EjectAgitatorCommand(agitatorManagerSubsystem.getRightAgitator());
        
        intakeLeft.includeOnSmartDashboard("Left Agitator Intake");
        intakeRight.includeOnSmartDashboard("Right Agitator Intake");
        
        ejectLeft.includeOnSmartDashboard("Left Agitator Eject");
        ejectRight.includeOnSmartDashboard("Right Agitator Eject");
        
        
        oi.controller.getXboxButton(XboxButton.LeftStick).whileHeld(unjamLeft);
        oi.controller.getXboxButton(XboxButton.RightStick).whileHeld(unjamRight);
        
        //oi.operatorPanelButtons.getifAvailable(9).whileHeld(intakeLeft);
        //oi.operatorPanelButtons.getifAvailable(8).whileHeld(ejectLeft);
        //oi.operatorPanelButtons.getifAvailable(7).whileHeld(intakeRight);  
        //oi.operatorPanelButtons.getifAvailable(6).whileHeld(ejectRight);  
    }
    
    @Inject
    public void setupDriveCommand(
            OperatorInterface oi,
            DriveForDistanceCommand driveForDistance, 
            ResetDistanceCommand resetDisplacement,
            RotateToHeadingCommand rotateToHeading,
            SetRobotHeadingCommand setHeading,
            DriveToShootingRangeCommand driveToShootingRange,
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
        
        oi.rightButtons.getIfAvailable(2).whileHeld(driveToShootingRange);
    }
    
    @Inject
    public void setupVisionCommands(
            OperatorInterface oi,
            RotateRobotToBoilerCommand rotateCommand,
            DriveTowardBoilerWithVisionAndJoysticksCommand driverControlledBoilerApproachCommand,
            DriveToBoilerUsingHeuristicsWithVisionCommandGroup driveToBoilerCommand,
            SetRobotHeadingCommand setBlueHeadingCommand,
            SetRobotHeadingCommand setRedHeadingCommand,
            RotateToHeadingCommand rotateToHeadingCommand
    )   {
        oi.leftButtons.getIfAvailable(2).whileHeld(rotateCommand);
        driverControlledBoilerApproachCommand.setControlJoystick(oi.leftJoystick);
        oi.leftButtons.getIfAvailable(3).whileHeld(driverControlledBoilerApproachCommand);
        oi.leftButtons.getIfAvailable(10).whileHeld(driveToBoilerCommand);

        setBlueHeadingCommand.setHeadingToApply(-135);
        setBlueHeadingCommand.includeOnSmartDashboard("Set heading to blue boiler orientation (-135)");

        setRedHeadingCommand.setHeadingToApply(-45);
        setRedHeadingCommand.includeOnSmartDashboard("Set heading to red boiler orientation (-45)");
    }

    @Inject
    public void setupAutonomous(
            OperatorInterface oi,
            DisableAutonomousCommand disableCommand,
            SetupShootAndDriveAcrossBaseLineCommand shootThenBaseline,
            SetupDriveToHopperThenBoilerCommand driveToBoiler,
            SetupBreakBaselineCommand breakBaseLine)
    {
        oi.leftButtons.getIfAvailable(8).whenPressed(breakBaseLine);
        oi.leftButtons.getIfAvailable(9).whenPressed(driveToBoiler);
        oi.rightButtons.getIfAvailable(8).whenPressed(disableCommand);
        oi.rightButtons.getIfAvailable(9).whenPressed(shootThenBaseline);
        
        breakBaseLine.includeOnSmartDashboard("Break Base Line");
        disableCommand.includeOnSmartDashboard("Disable Autonomous");
        driveToBoiler.includeOnSmartDashboard("Run DriveToBoiler Autonomous Command");
        shootThenBaseline.includeOnSmartDashboard("Shoot then baseline autonomous command");
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
        //oi.operatorPanelButtons.getifAvailable(1).whileHeld(shootFuel);
        //oi.operatorPanelButtons.getifAvailable(10).whileHeld(shootLeftFuel);
        //oi.operatorPanelButtons.getifAvailable(11).whileHeld(shootRightFuel);
    }
    
    @Inject
    public void setupFeedingCommandGroup(
            OperatorInterface oi,
            LeftFeedingCommandGroup feedLeft,
            RightFeedingCommandGroup feedRight) {

        oi.controller.getXboxButton(XboxButton.LeftBumper).whileHeld(feedLeft);
        oi.controller.getXboxButton(XboxButton.RightBumper).whileHeld(feedRight);
    }
}