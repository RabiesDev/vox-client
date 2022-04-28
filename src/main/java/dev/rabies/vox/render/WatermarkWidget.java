package dev.rabies.vox.render;

import dev.rabies.vox.Constants;
import dev.rabies.vox.events.Render2DEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

public class WatermarkWidget extends Widget {
    public WatermarkWidget() {
        super("Watermark");
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void draw(Render2DEvent event) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        font.drawStringWithShadow(
                Constants.MOD_NAME,
                4, 4,
                new Color(120, 255, 70).getRGB()
        );
    }

    @Override
    public void onInputKey(int keyCode, boolean state) {
    }
}
