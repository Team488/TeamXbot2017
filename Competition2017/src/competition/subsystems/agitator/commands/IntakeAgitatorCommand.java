package competition.subsystems.agitator.commands;

import competition.subsystems.agitator.AgitatorSubsystem;
import competition.subsystems.agitator.commands.IntakeAgitatorCommand;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class IntakeAgitatorCommand extends BaseAgitatorCommand {
    private AgitatorSubsystem agitatorSubsystem;
    public DoubleProperty motorCurrentThreshold;
    private DoubleProperty unjamTimeInterval;
    private double mostRecentEjectTime;
    private double currentTime;
    private boolean mostRecentEjectTimeSet = false;
    
    public IntakeAgitatorCommand(AgitatorSubsystem agitatorSubsystem, XPropertyManager propManager) {
        super(agitatorSubsystem);
        this.agitatorSubsystem = agitatorSubsystem;
        motorCurrentThreshold = propManager
                .createPersistentProperty(" threshold for agitator motor current before unjamming", 10);
        unjamTimeInterval = propManager.createPersistentProperty(" time interval for unjamming agitator", 13);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute(){
        currentTime = Timer.getFPGATimestamp();
        if (agitatorSubsystem.getMotorCurrent() >= motorCurrentThreshold.get()) {
            mostRecentEjectTimeSet = true;
            mostRecentEjectTime = Timer.getFPGATimestamp();
            agitatorSubsystem.eject();
        } else if (mostRecentEjectTimeSet && (currentTime - mostRecentEjectTime) < unjamTimeInterval.get()) {
            agitatorSubsystem.eject();
        } else {
            mostRecentEjectTimeSet = false;
            agitatorSubsystem.intake();
        }
    }    
}