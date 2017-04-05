package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;

import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class UnjamAgitatorCommand extends BaseAgitatorCommand {

    protected final DoubleProperty motorCurrentThreshold;
    protected final DoubleProperty unjammingEjectPower;
    
    public UnjamAgitatorCommand(AgitatorSubsystem agitatorSubsystem, XPropertyManager propMan) {
        super(agitatorSubsystem);
        
        motorCurrentThreshold = propMan.createPersistentProperty("Threshold for jammed agitator motor speed", 0.488);
        unjammingEjectPower = propMan.createPersistentProperty("Eject power for unjamming agitator", -1);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double currentAgitatorMotorSpeed = agitatorSubsystem.getMotorSpeed();
        if (currentAgitatorMotorSpeed >= 0 && currentAgitatorMotorSpeed <= motorCurrentThreshold.get()) {
            agitatorSubsystem.setAgitatorPower(unjammingEjectPower.get());
        }
    }
}
