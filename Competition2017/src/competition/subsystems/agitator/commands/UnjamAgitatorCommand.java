package competition.subsystems.agitator.commands;

import com.google.inject.Inject;

import competition.subsystems.agitator.AgitatorSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class UnjamAgitatorCommand extends BaseAgitatorCommand {
    private AgitatorSubsystem agitatorSubsystem;
    public DoubleProperty motorCurrentThreshold;
    private DoubleProperty unjamTimeInterval;
    private double initialEjectTime;
    private double currentTime;
    private boolean initialEjectTimeSet = false;

    @Inject
    public UnjamAgitatorCommand(AgitatorSubsystem agitatorSubsystem, XPropertyManager propManager) {
        super(agitatorSubsystem);
        this.agitatorSubsystem = agitatorSubsystem;
        motorCurrentThreshold = propManager
                .createPersistentProperty(" threshold for agitator motor current before unjamming", 10);
        unjamTimeInterval = propManager.createPersistentProperty(" time interval for unjamming agitator", 13);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        currentTime = Timer.getFPGATimestamp();
        if (agitatorSubsystem.getMotorCurrent() >= motorCurrentThreshold.get()) {
            initialEjectTimeSet = true;
            initialEjectTime = Timer.getFPGATimestamp();
            agitatorSubsystem.eject();
        } else if (currentTime - initialEjectTime < unjamTimeInterval.get() && initialEjectTimeSet) {
            agitatorSubsystem.eject();
        } else {
            initialEjectTimeSet = false;
            agitatorSubsystem.intake();
        }
    }
}
