package dev.rabies.vox.cheats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;

public interface ICheat {

    Minecraft mc = Minecraft.getMinecraft();
    EntityPlayerSP player = mc.player;
    World world = mc.world;

    default void onEnable() {}

    default void onDisable() {}

    void toggle();
}
