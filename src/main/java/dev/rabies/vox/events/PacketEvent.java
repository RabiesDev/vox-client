package dev.rabies.vox.events;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

public class PacketEvent extends VoxEvent {

    @Getter @Setter
    private Packet<?> packet;

    public PacketEvent(VoxEventTiming timing, Packet<?> packetIn) {
        super(timing);
        this.packet = packetIn;
    }

    public boolean isOut() {
        return getTiming() == VoxEventTiming.PRE;
    }

    public boolean isIn() {
        return getTiming() == VoxEventTiming.POST;
    }
}
