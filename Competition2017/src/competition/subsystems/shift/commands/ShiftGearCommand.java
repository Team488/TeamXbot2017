package competition.subsystems.shift.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.command.BaseCommand;

public class ShiftGearCommand extends BaseCommand {
    private static Logger log = Logger.getLogger(ShiftGearCommand.class);
    
    private ShiftSubsystem shiftSubsystem;
    private Gear gear;
    
    @Inject
    public ShiftGearCommand(ShiftSubsystem shiftSubsystem) {
        this.requires(shiftSubsystem);
        
        this.shiftSubsystem = shiftSubsystem;
    }
    
    public void setGear(Gear gear) {
        this.gear = gear;
    }
    
    @Override
    public void initialize() {
        if (gear == Gear.LOW_GEAR) {
            log.info("Shifting to low gear");
        } else if (gear == Gear.HIGH_GEAR){
            log.info("Shifting to high gear");
        } else {
            log.info("Defaulting to low gear");
        }
        
        shiftSubsystem.setGear(gear);
    }

    @Override
    public void execute() {
        
    }
}
