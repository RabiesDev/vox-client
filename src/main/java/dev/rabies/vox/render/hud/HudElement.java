package dev.rabies.vox.render.hud;

import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.render.RenderHook;
import lombok.Getter;

public abstract class HudElement {

    @Getter
    private final String name;
    @Getter
    protected final ElementBox box;

    public HudElement(String name) {
        this.name = name;
        this.box = new ElementBox();
    }

    public abstract boolean isVisible();

    public abstract void render(RenderHook hook, Render2DEvent event);
}
