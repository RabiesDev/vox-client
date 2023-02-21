package dev.rabies.vox.events;

import lombok.Getter;
import net.minecraftforge.fml.common.eventhandler.Event;

public class VoxEvent extends Event {
    @Getter
    private final EventTiming timing;

    public VoxEvent() {
        this.timing = EventTiming.PRE;
    }

    public VoxEvent(EventTiming timing) {
        this.timing = timing;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public boolean isPre() {
        return timing == EventTiming.PRE;
    }

    public boolean isPost() {
        return timing == EventTiming.POST;
    }
}
