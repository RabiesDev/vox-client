package dev.rabies.vox.events.game;

import dev.rabies.vox.events.VoxEvent;
import dev.rabies.vox.events.EventTiming;

public class UpdateEvent extends VoxEvent {
    public UpdateEvent(EventTiming eventType) {
        super(eventType);
    }
}
