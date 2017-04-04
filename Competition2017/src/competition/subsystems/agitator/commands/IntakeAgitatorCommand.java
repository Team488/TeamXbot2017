package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class IntakeAgitatorCommand extends BaseAgitatorCommand {
    private AgitatorSubsystem agitatorSubsystem;
    
    protected final DoubleProperty motorCurrentThreshold;
    protected final DoubleProperty unjamTimeInterval;
    protected final DoubleProperty minSecForAgitatorToEject;
    
    private double mostRecentEjectTime;
    private double currentTime;
    private boolean mostRecentEjectTimeSet = false;
    
    private boolean agitatorCanEnable = false;
    private double agitatorPassedThresholdTime;
    
    public IntakeAgitatorCommand(AgitatorSubsystem agitatorSubsystem, XPropertyManager propManager) {
        super(agitatorSubsystem);
        this.agitatorSubsystem = agitatorSubsystem;
        motorCurrentThreshold = propManager
                .createPersistentProperty("Threshold for agitator motor current before unjamming", 10);
        unjamTimeInterval = propManager.createPersistentProperty(" time interval for unjamming agitator", 13);
        minSecForAgitatorToEject = propManager
                .createPersistentProperty("Minimum seconds after agitator is pass the threshold to run", 1);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute(){
        currentTime = Timer.getFPGATimestamp();
        if (agitatorSubsystem.getMotorCurrent() >= motorCurrentThreshold.get()) {
            toggleTimerSinceMotorPassedThreshold();
            if (agitatorCanEnable) {
                mostRecentEjectTimeSet = true;
                mostRecentEjectTime = Timer.getFPGATimestamp();
                agitatorSubsystem.eject();
                resetAgitatorPassedThresholdTime();
            }
        } else if (mostRecentEjectTimeSet && (currentTime - mostRecentEjectTime) < unjamTimeInterval.get()) {
            agitatorSubsystem.eject();
        } else {
            mostRecentEjectTimeSet = false;
            agitatorSubsystem.intake();
        }
    }
    
    private void toggleTimerSinceMotorPassedThreshold() {
        if (!agitatorCanEnable && agitatorPassedThresholdTime <= 0) {
            agitatorPassedThresholdTime = Timer.getFPGATimestamp();
        } else if (currentTime - agitatorPassedThresholdTime <= minSecForAgitatorToEject.get()) {
            agitatorCanEnable = true;
        } else {
            agitatorCanEnable = false;
        }
    }
    
    private void resetAgitatorPassedThresholdTime() {
        agitatorPassedThresholdTime = 0;
    }
}