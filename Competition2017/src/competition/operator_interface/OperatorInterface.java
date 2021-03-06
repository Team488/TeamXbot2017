package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.controls.sensors.XXboxController;
import xbot.common.controls.sensors.JoystickButtonManager;
import xbot.common.controls.sensors.XJoystick;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.logging.RobotAssertionManager;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands and command groups
 * that allow control of the robot.
 */
@Singleton
public class OperatorInterface {
    public XJoystick leftJoystick;
    public XJoystick rightJoystick;
    public XJoystick operatorPanelJoystick;
    
    public XXboxController controller;
  
    public JoystickButtonManager leftButtons;
    public JoystickButtonManager rightButtons;
    public JoystickButtonManager operatorPanelButtons;
    
    @Inject
    public OperatorInterface(WPIFactory factory, RobotAssertionManager assertionManager) {
        controller = factory.getXboxController(0);
        
        leftJoystick = factory.getJoystick(1);
        rightJoystick = factory.getJoystick(2);
        operatorPanelJoystick = factory.getJoystick(3);
        
        leftJoystick.setYInversion(true);
        rightJoystick.setYInversion(true);
        
        leftButtons = new JoystickButtonManager(12, factory, assertionManager, leftJoystick);
        rightButtons = new JoystickButtonManager(12, factory, assertionManager, rightJoystick);
        operatorPanelButtons = new JoystickButtonManager(11, factory, assertionManager, operatorPanelJoystick);
    }
}
