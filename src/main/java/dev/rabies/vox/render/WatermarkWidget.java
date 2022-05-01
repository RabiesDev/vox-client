package dev.rabies.vox.render;

import dev.rabies.vox.Constants;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.events.Render2DEvent;
import dev.rabies.vox.render.font.SystemFontRenderer;

import java.awt.*;

public class WatermarkWidget extends Widget {

    private final SystemFontRenderer watermarkFont = VoxMod.get().newSystemFont("Mukta-Bold", 28);

    public WatermarkWidget() {
        super("Watermark");
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void draw(Render2DEvent event) {
        watermarkFont.drawStringWithShadow(Constants.MOD_NAME, 5, 0, new Color(120, 255, 70).getRGB());
    }

    @Override
    public void onInputKey(int keyCode, boolean state) {
    }
}
