package dev.rabies.vox.events.render;

import dev.rabies.vox.events.VoxEvent;
import dev.rabies.vox.events.VoxEventTiming;
import lombok.Getter;
import net.minecraft.entity.Entity;

public class RenderEntityEvent extends VoxEvent {

    @Getter
    private final RenderCallback<RenderEntityEvent> callback;
    @Getter
    private final Entity entity;

    public RenderEntityEvent(VoxEventTiming timing, Entity entity, RenderCallback<RenderEntityEvent> callback) {
        super(timing);
        this.entity = entity;
        this.callback = callback;
    }
}
