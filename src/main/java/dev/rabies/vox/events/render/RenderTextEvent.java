package dev.rabies.vox.events.render;

import dev.rabies.vox.events.VoxEvent;
import dev.rabies.vox.events.VoxEventTiming;
import lombok.Getter;
import lombok.Setter;

public class RenderTextEvent extends VoxEvent {

    @Getter @Setter
    private String string;

    public RenderTextEvent(VoxEventTiming timing, String string) {
        super(timing);
        this.string = string;
    }
}
