package dev.rabies.vox.events;

import lombok.Getter;
import net.minecraftforge.fml.common.eventhandler.Event;

public class VoxEvent extends Event {

    @Getter private final VoxEventTiming timing;

    public VoxEvent() {
        this.timing = VoxEventTiming.PRE;
    }

    public VoxEvent(VoxEventTiming timing) {
        this.timing = timing;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public boolean isPre() {
        return timing == VoxEventTiming.PRE;
    }

    public boolean isPost() {
        return timing == VoxEventTiming.POST;
    }
}
