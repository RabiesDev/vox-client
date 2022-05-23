package dev.rabies.vox.utils.render;

import dev.rabies.vox.events.game.WindowResizeEvent;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RefreshFramebuffer extends Framebuffer {

    public RefreshFramebuffer(int width, int height, boolean useDepthIn) {
        super(width, height, useDepthIn);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWindowResize(WindowResizeEvent event) {
        createBindFramebuffer(event.getWidth(), event.getHeight());
//        this.framebufferWidth = event.getWidth();
//        this.framebufferHeight = event.getHeight();
    }
}
