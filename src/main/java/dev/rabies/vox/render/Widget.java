package dev.rabies.vox.render;

import dev.rabies.vox.events.render.Render2DEvent;
import lombok.Getter;

public abstract class Widget {

    @Getter
    private final String name;

    public Widget(String name) {
        this.name = name;
    }

    public abstract boolean isVisible();

    public abstract void draw(Render2DEvent event);

    public abstract void onInputKey(int keyCode, boolean state);
}
