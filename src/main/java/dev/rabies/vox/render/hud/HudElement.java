package dev.rabies.vox.render.hud;

import dev.rabies.vox.events.render.RenderOverlayEvent;
import dev.rabies.vox.render.RenderHook;
import lombok.Getter;

public abstract class HudElement {
    @Getter
    private final String name;
    @Getter
    protected final Dimension box;

    public HudElement(String name) {
        this.name = name;
        this.box = new Dimension();
    }

    public abstract boolean isVisible();

    public abstract void render(RenderHook hook, RenderOverlayEvent event);
}
