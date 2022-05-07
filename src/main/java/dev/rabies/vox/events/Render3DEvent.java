package dev.rabies.vox.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Render3DEvent extends VoxEvent {

    private final float partialTicks;
}
