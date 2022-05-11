package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.CheatWrapper;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.events.render.Render3DEvent;
import dev.rabies.vox.utils.ShaderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL20;

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
