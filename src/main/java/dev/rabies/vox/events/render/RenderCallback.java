package dev.rabies.vox.events.render;

import dev.rabies.vox.events.VoxEvent;

@FunctionalInterface
public interface RenderCallback<T extends VoxEvent> {
    void render(T event);
}
