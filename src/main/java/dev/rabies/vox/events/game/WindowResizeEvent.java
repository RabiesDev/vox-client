package dev.rabies.vox.events.game;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraftforge.fml.common.eventhandler.Event;

@EqualsAndHashCode(callSuper = true)
@Data
public class WindowResizeEvent extends Event {
    private final int width, height;
}
