package dev.rabies.vox.events.render;

import dev.rabies.vox.events.VoxEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RenderWorldEvent extends VoxEvent {
    private final float partialTicks;
}
