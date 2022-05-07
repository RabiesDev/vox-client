package dev.rabies.vox.events.render;

import dev.rabies.vox.events.VoxEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.gui.ScaledResolution;

@EqualsAndHashCode(callSuper = true)
@Data
public class Render2DEvent extends VoxEvent {

    private final ScaledResolution resolution;
    private final float partialTicks;
}
