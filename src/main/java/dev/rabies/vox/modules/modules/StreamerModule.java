package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.events.render.RenderTextEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StreamerModule extends Module {
    public StreamerModule() {
        super("Streamer", Category.OTHER);
    }

    @SubscribeEvent
    public void onRenderText(RenderTextEvent event) {
        String text = event.getString();
        if (mc.player != null && text.contains(mc.player.getName())) {
            event.setString(text.replace(mc.player.getName(), "Vox User"));
        }
    }
}
