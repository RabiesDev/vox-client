package dev.rabies.vox.events.game;

import dev.rabies.vox.events.VoxEvent;
import dev.rabies.vox.events.VoxEventTiming;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

public class PacketEvent extends VoxEvent {

    @Getter
    private final Packet<?> packet;

    public PacketEvent(VoxEventTiming timing, Packet<?> packetIn) {
        super(timing);
        this.packet = packetIn;
    }
    
    @Override
    public boolean isCancelable() {
    	return true;
    }

    public boolean isOut() {
        return getTiming() == VoxEventTiming.PRE;
    }

    public boolean isIn() {
        return getTiming() == VoxEventTiming.POST;
    }
}
