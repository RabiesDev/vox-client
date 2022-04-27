package dev.rabies.vox.render;

import dev.rabies.vox.events.Render2DEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UIHook {

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        renderWatermark(event);
        renderChestInfo(event);
    }

    private void renderWatermark(Render2DEvent event) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        ScaledResolution resolution = event.getResolution();
        font.drawStringWithShadow(
                "watermark momento",
                10,
                resolution.getScaledHeight() - font.FONT_HEIGHT - 2,
                0xff
        );
    }

    private void renderChestInfo(Render2DEvent event) {
        // TODO:
    }
}
