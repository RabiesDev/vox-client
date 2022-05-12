package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.events.render.RenderTextEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StreamerCheat extends CheatWrapper {

    public StreamerCheat() {
        super("Streamer", Category.OTHER);
    }

    @SubscribeEvent
    public void onRenderText(RenderTextEvent event) {
        String string = event.getString();
        if (!string.contains(mc.player.getName())) return;
        event.setString(string.replace(mc.player.getName(), "§aVox"));
    }
}
