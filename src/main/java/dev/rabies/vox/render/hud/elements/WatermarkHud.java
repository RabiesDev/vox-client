package dev.rabies.vox.render.hud.elements;

import dev.rabies.vox.Constants;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.render.RenderHook;
import dev.rabies.vox.render.font.SystemFontRenderer;
import dev.rabies.vox.render.hud.HudElement;

import java.awt.*;

public class WatermarkHud extends HudElement {

    private final SystemFontRenderer watermarkFont = VoxMod.get().newSystemFont("NotoSansJP-Bold", 28);

    public WatermarkHud() {
        super("Watermark");
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void render(RenderHook hook, Render2DEvent event) {
        watermarkFont.drawStringWithShadow(Constants.MOD_NAME, 5, 0, new Color(120, 255, 70).getRGB());
        box.setSize(watermarkFont.getStringWidth(Constants.MOD_NAME), watermarkFont.getHeight());
    }
}
