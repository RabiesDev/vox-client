package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import lombok.Getter;

public class BrightnessModule extends Module {
    @Getter
    private float lastGamma;

    public BrightnessModule() {
        super("Brightness", Category.OTHER);
    }

    @Override
    public void onEnable() {
        lastGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1000.0f;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = lastGamma;
    }
}
