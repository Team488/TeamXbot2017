package competition.subsystems.agitator;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.ctre.CANTalon.TalonControlMode;
import com.google.inject.Inject;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
@Singleton
public class AgitatorSubsystem extends BaseSubsystem{
    private static Logger log = Logger.getLogger(AgitatorSubsystem.class);
    
    public final DoubleProperty motorPowerProperty;
    public final XCANTalon agitatorMotor;
    
    @Inject
    public AgitatorSubsystem(WPIFactory factory, XPropertyManager propManager){
        log.info("Creating Agitator Subsystem");
        agitatorMotor = factory.getCANTalonSpeedController(6);
        agitatorMotor.setBrakeEnableDuringNeutral(true);
        agitatorMotor.setProfile(0);
        agitatorMotor.setControlMode(TalonControlMode.PercentVbus);
        motorPowerProperty = propManager.createPersistentProperty("Agitator motor power", 0.5);
    }
    
    public void stop(){
        agitatorMotor.set(0);
    }
    
    public void spinForwards(){
        agitatorMotor.set(motorPowerProperty.get());
    }
    
    public void spinBackwards(){
        agitatorMotor.set(motorPowerProperty.get() * - 1);
    }
    
    

}
