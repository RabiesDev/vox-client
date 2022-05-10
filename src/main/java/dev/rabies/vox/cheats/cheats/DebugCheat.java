package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.CheatWrapper;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.events.render.RenderModelEvent;
import dev.rabies.vox.utils.ShaderUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DebugCheat extends CheatWrapper {
	
	private final ShaderUtil outlineShader = new ShaderUtil("outline_shader.frag");
	private Framebuffer framebuffer;

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
    
    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
    }

    @SubscribeEvent
    public void onRenderEntity(RenderModelEvent event) {
        if (!(event.getLivingBase() instanceof EntityPlayer)) return;
        if (event.isPre()) {
        }
    }
    
    private Framebuffer createFramebuffer() {
    	if (framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
    		if (framebuffer != null) framebuffer.deleteFramebuffer();
    		framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
    	}
    	return framebuffer;
    }
}
