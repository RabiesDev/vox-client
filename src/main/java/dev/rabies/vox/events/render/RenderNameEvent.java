package dev.rabies.vox.events.render;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderNameEvent extends Event {

    @Override
    public boolean isCancelable() {
        return true;
    }
}
