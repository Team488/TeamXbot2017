package competition.subsystems.offboard.commands;

import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import xbot.common.command.BaseCommand;

public abstract class OffboardProcessingCommand extends BaseCommand {
    
    protected final int commandId;
    protected final OffboardInterfaceSubsystem subsystem;
    
    private boolean isRemoteFinished = false;
    
    private OffboardProcessingCommand(int commandId, OffboardInterfaceSubsystem subsystem) {
        this.commandId = commandId;
        this.subsystem = subsystem;
        
        this.requires(subsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Sending remote start for command ID 0x" + Integer.toHexString(commandId).toUpperCase());
        subsystem.sendSetCurrentCommand(commandId);
    }
    
    protected abstract void handleIncomingPacket(OffboardCommunicationPacket packet);
    
    @Override
    public final void execute() {
        // TODO: Tune loop? Logging when hit limit?
        for(int receiveCount = 0; receiveCount < 5; receiveCount++) {
            OffboardCommunicationPacket packet = subsystem.rawCommsInterface.receiveRaw();
            if(packet == null) {
                break;
            }
            
            handleIncomingPacket(packet);
        }
    }
    
    @Override
    public boolean isFinished() {
        return isRemoteFinished;
    }
    
    @Override
    public void interrupted() {
        log.info("Interrupted; stopping remote command");
        subsystem.sendSetCurrentCommand(0);
    }
}
