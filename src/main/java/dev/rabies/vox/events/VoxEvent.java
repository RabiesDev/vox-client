package dev.rabies.vox.events;

import lombok.Getter;
import net.minecraftforge.fml.common.eventhandler.Event;

public class VoxEvent extends Event {

    @Getter private final VoxEventTiming timing;

    public VoxEvent(VoxEventTiming timing) {
        this.timing = timing;
    }

    public boolean isPre() {
        return timing == VoxEventTiming.PRE;
    }

    public boolean isPost() {
        return timing == VoxEventTiming.POST;
    }
}
