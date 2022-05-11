package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.events.render.Render3DEvent;
import dev.rabies.vox.utils.ShaderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL20;

public class OutlineEspCheat extends CheatWrapper {

    private final ShaderUtil outlineShader = new ShaderUtil("outline_shader.frag");
    private Framebuffer framebuffer;

    public OutlineEspCheat() {
        super("Outline ESP", Category.OTHER);
    }

    private void setupUni(int direction1, int direction2) {
        GL20.glUniform1i(outlineShader.getUniformByName("u_texture"), 0);
        GL20.glUniform1f(outlineShader.getUniformByName("u_radius"), 1.5f);
        GL20.glUniform2f(outlineShader.getUniformByName("u_texelSize"), 1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
        GL20.glUniform2f(outlineShader.getUniformByName("u_direction"), direction1, direction2);
        GL20.glUniform3f(outlineShader.getUniformByName("u_color"), 0.4f, 0.6f, 1.0f);
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        if (framebuffer == null || !outlineShader.isBinded()) return;
        mc.getFramebuffer().bindFramebuffer(true);
        GL20.glUseProgram(outlineShader.getProgramId());
        setupUni(0, 1);
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        outlineShader.renderShader(event.getResolution());

        GL20.glUseProgram(outlineShader.getProgramId());
        setupUni(1, 0);
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        outlineShader.renderShader(event.getResolution());
    }

    @SubscribeEvent
    public void onRender3d(Render3DEvent event) {
        framebuffer = createFramebuffer();
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        renderEntities(event.getPartialTicks());
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);
        mc.entityRenderer.disableLightmap();
        GlStateManager.disableLighting();
    }

    private Framebuffer createFramebuffer() {
        if (framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
            if (framebuffer != null) framebuffer.deleteFramebuffer();
            framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        }
        return framebuffer;
    }

    private void renderEntities(float partialTicks) {
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (!(entity instanceof EntityPlayer)) continue;
            if (entity == mc.player && mc.gameSettings.thirdPersonView == 0) return;
            mc.getRenderManager().setRenderShadow(false);
            mc.entityRenderer.disableLightmap();
            mc.getRenderManager().renderEntityStatic(entity, partialTicks, true);
            mc.getRenderManager().setRenderShadow(true);
            outlineShader.setBinded(true);
        }
    }
}
