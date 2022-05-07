package dev.rabies.vox.events.game;

import dev.rabies.vox.events.VoxEvent;
import dev.rabies.vox.events.VoxEventTiming;

public class UpdateEvent extends VoxEvent {

    public UpdateEvent(VoxEventTiming eventType) {
        super(eventType);
    }
}
