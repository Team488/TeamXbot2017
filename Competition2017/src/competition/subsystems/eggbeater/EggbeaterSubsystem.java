package competition.subsystems.eggbeater;

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
public class EggbeaterSubsystem extends BaseSubsystem{
    private static Logger log = Logger.getLogger(EggbeaterSubsystem.class);
    
    public final DoubleProperty motorPowerProperty;
    public final XCANTalon eggbeaterMotor;
    
    @Inject
    public EggbeaterSubsystem(WPIFactory factory, XPropertyManager propManager){
        log.info("Creating Eggbeater Subsystem");
        eggbeaterMotor = factory.getCANTalonSpeedController(6);
        eggbeaterMotor.setBrakeEnableDuringNeutral(true);
        eggbeaterMotor.setProfile(0);
        eggbeaterMotor.setControlMode(TalonControlMode.PercentVbus);
        motorPowerProperty = propManager.createPersistentProperty("Eggbeater motor power", 0.5);
    }
    
    public void stop(){
        eggbeaterMotor.set(0);
    }
    
    public void spinForwards(){
        eggbeaterMotor.set(motorPowerProperty.get());
    }
    
    public void spinBackwards(){
        eggbeaterMotor.set(motorPowerProperty.get() * - 1);
    }
    
    

}
