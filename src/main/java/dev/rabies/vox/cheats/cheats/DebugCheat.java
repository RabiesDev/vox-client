package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.CheatWrapper;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;

public class DebugCheat extends CheatWrapper {

    public DebugCheat() {
        super("Debug", Category.OTHER);
    }

    @Override
    public void onEnable() {
        VoxMod.get().setDebugMode(true);
    }

    @Override
    public void onDisable() {
        VoxMod.get().setDebugMode(false);
    }
}
