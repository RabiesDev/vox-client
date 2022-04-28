package dev.rabies.vox.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.gui.ScaledResolution;

@EqualsAndHashCode(callSuper = true)
@Data
public class Render2DEvent extends VoxEvent {

    private final ScaledResolution resolution;
}
