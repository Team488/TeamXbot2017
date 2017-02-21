package competition.subsystems.shooter_belt;

import competition.InfluxDBWriter;
import competition.subsystems.BaseXCANTalonSpeedControlledSubsystem;
import competition.subsystems.RobotSide;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ShooterBeltSubsystem extends BaseXCANTalonSpeedControlledSubsystem {

    private final RobotSide side;
    protected final DoubleProperty intakePowerProperty;
    protected final DoubleProperty ejectPowerProperty;
    protected final DoubleProperty beltIntakeTargetSpeed;
    
    private InfluxDBWriter influxWriter;
    
    public ShooterBeltSubsystem(
            RobotSide side,
            int masterChannel,
            boolean invertMaster,
            boolean invertMasterSensor,
            WPIFactory factory, 
            PIDPropertyManager pidPropertyManager,
            XPropertyManager propManager,
            InfluxDBWriter influxWriter) {
        super(
                side + " ShooterBelt",
                masterChannel,
                invertMaster,
                invertMasterSensor,
                factory,
                pidPropertyManager,
                propManager);
        this.side = side;
        
        intakePowerProperty = propManager.createPersistentProperty("ShooterBelt intake power", 0.5);
        ejectPowerProperty = propManager.createPersistentProperty("ShooterBelt eject power", -0.5);
        beltIntakeTargetSpeed = propManager.createPersistentProperty("ShooterBelt intake speed", 100);
        
        this.influxWriter = influxWriter;
    }

    public RobotSide getSide() {
        return side;
    }
    
    public void eject() {
        setPower(ejectPowerProperty.get());
    }
    
    public void intakeUsingPower() {
        setPower(intakePowerProperty.get());
    }
    
    public void intakeUsingSpeed() {
        setTargetSpeed(beltIntakeTargetSpeed.get());
    }
    
    public void stop(){
        setPower(0);
    }
    
    @Override
    public void updatePeriodicData() {
        super.updatePeriodicData();
        if (side == RobotSide.Left) {
            this.influxWriter.writeData("leftSideShooterBeltPower", masterMotor.get());
            this.influxWriter.writeData("leftSideShooteBeltCurrent", masterMotor.getOutputCurrent());
            this.influxWriter.writeData("leftSideShooterBeltFollowerPower", followerMotor.get());
            this.influxWriter.writeData("leftSideShooteBeltFollowerCurrent", followerMotor.getOutputCurrent());
        } else if (side == RobotSide.Right) {
            this.influxWriter.writeData("rightSideShooterBeltPower", masterMotor.get());
            this.influxWriter.writeData("rightSideShooteBeltCurrent", masterMotor.getOutputCurrent());
            this.influxWriter.writeData("rightSideShooterFollowerBeltPower", followerMotor.get());
            this.influxWriter.writeData("rightSideShooteBeltFollowerCurrent", followerMotor.getOutputCurrent());
        }
    }
}