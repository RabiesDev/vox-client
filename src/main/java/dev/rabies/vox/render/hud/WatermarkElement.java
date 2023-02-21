package dev.rabies.vox.render.hud;

import dev.rabies.vox.Constants;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.events.render.RenderOverlayEvent;
import dev.rabies.vox.render.RenderHook;
import dev.rabies.vox.render.font.SystemFontRenderer;
import dev.rabies.vox.render.hud.HudElement;

import java.awt.*;

public class WatermarkElement extends HudElement {
    private final SystemFontRenderer font = VoxMod.newSystemFont("Inter-Bold", 28);

    public WatermarkElement() {
        super("Watermark");
    }

    @Override
    public void render(RenderHook hook, RenderOverlayEvent event) {
        font.drawStringWithShadow(Constants.MOD_NAME, 6, 2, new Color(120, 255, 70).getRGB());
        box.setSize(font.getStringWidth(Constants.MOD_NAME), font.getHeight());
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
