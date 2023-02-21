package dev.rabies.vox.events.game;

import dev.rabies.vox.events.VoxEvent;
import dev.rabies.vox.events.EventTiming;
import lombok.Getter;
import net.minecraft.network.Packet;

public class PacketEvent extends VoxEvent {
    @Getter
    private final Packet<?> packet;

    public PacketEvent(EventTiming timing, Packet<?> packetIn) {
        super(timing);
        this.packet = packetIn;
    }
    
    @Override
    public boolean isCancelable() {
    	return true;
    }

    public boolean isOut() {
        return getTiming() == EventTiming.PRE;
    }

    public boolean isIn() {
        return getTiming() == EventTiming.POST;
    }
}
