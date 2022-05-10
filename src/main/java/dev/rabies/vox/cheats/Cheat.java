package dev.rabies.vox.cheats;

import net.minecraft.client.Minecraft;

public interface Cheat {

    Minecraft mc = Minecraft.getMinecraft();

    default void onEnable() {}

    default void onDisable() {}

    void toggle();
}
