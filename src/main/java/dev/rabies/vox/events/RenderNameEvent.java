package dev.rabies.vox.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderNameEvent extends Event {

    @Override
    public boolean isCancelable() {
        return true;
    }
}
