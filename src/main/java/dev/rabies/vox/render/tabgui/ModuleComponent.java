package dev.rabies.vox.render.tabgui;

import dev.rabies.vox.modules.Module;
import lombok.Getter;
import org.lwjgl.input.Keyboard;

public class ModuleComponent implements InputListener {
    @Getter
    private final Module module;
    @Getter
    private final String label;

    public ModuleComponent(Module module) {
        this.module = module;
        this.label = module.getName();
    }

    @Override
    public void input(int keyCode, boolean state) {
        if (state && (keyCode == Keyboard.KEY_RIGHT || keyCode == Keyboard.KEY_RETURN)) {
            module.toggle();
        }
    }
}
