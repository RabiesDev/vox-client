package dev.rabies.vox.events;

import lombok.Getter;
import net.minecraft.network.Packet;

public class PacketEvent extends VoxEvent {

    @Getter
    private final Packet<?> packet;

    public PacketEvent(VoxEventTiming timing, Packet<?> packetIn) {
        super(timing);
        this.packet = packetIn;
    }
}
