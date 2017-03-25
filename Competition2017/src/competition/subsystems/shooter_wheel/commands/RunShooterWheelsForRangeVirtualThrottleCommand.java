package competition.subsystems.shooter_wheel.commands;

import competition.subsystems.shooter_wheel.ShooterWheelSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelsManagerSubsystem;
import competition.subsystems.shooter_wheel.ShooterWheelSubsystem.TypicalShootingPosition;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class RunShooterWheelsForRangeVirtualThrottleCommand extends BaseShooterWheelCommand {

    protected final PIDManager pidManager;
    protected final double range;
    protected double throttlePower;
    
    public RunShooterWheelsForRangeVirtualThrottleCommand(TypicalShootingPosition range, ShooterWheelSubsystem shooterWheelSubsystem, PIDFactory pidFactory) {
        super("RunShooterWheelsForRangeVirtualThrottleCommand: " + range, shooterWheelSubsystem);
        this.range = shooterWheelSubsystem.translateTypicalShootingPositionToDistance(range);
        this.pidManager = pidFactory.createPIDManager("Shooter virtual throttle", 0.1, 0.0, 0.0, 0.0, 1.0, -0.5, 50.0, 0.0, 0.0);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        this.pidManager.reset();
        this.throttlePower = 0;
    }

    @Override
    public void execute() {
        double target = shooterWheelSubsystem.getTargetSpeedForRange(range);
        double current = shooterWheelSubsystem.getSpeed();
        
        double powerDelta = this.pidManager.calculate(target, current);
        throttlePower += powerDelta;
        
        throttlePower = MathUtils.constrainDoubleToRobotScale(throttlePower);
        
        shooterWheelSubsystem.setPower(throttlePower);
    }
}
