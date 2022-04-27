package dev.rabies.vox.events;

import lombok.Data;
import net.minecraft.client.gui.ScaledResolution;

@Data
public class Render2DEvent extends VoxEvent {

    private final ScaledResolution resolution;
}
