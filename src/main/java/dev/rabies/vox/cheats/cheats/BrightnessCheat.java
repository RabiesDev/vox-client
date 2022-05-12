package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import lombok.Getter;

public class BrightnessCheat extends CheatWrapper {

    @Getter
    private float prevGammaSetting;

    public BrightnessCheat() {
        super("Brightness", Category.OTHER);
    }

    @Override
    public void onEnable() {
        prevGammaSetting = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1000.0f;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = prevGammaSetting;
    }
}
