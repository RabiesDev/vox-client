package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.events.render.Render3DEvent;
import dev.rabies.vox.utils.ShaderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL20;

public class ChamsCheat extends CheatWrapper {

    private final BoolSetting ignoreSelfSetting = registerBoolSetting("Ignore self", false);
    private final BoolSetting invisSetting = registerBoolSetting("Invisible entity", true);

    private final ShaderUtil chamsShader = new ShaderUtil("chams_shader.frag");
    private boolean binded;
    private Framebuffer framebuffer;

    public ChamsCheat() {
        super("Chams", Category.OTHER);
    }
    
    private void setupUni() {
    	GL20.glUniform1i(chamsShader.getUniformByName("u_texture"), 0);
        GL20.glUniform1i(chamsShader.getUniformByName("u_coloring"), 1);
        GL20.glUniform1f(chamsShader.getUniformByName("u_alpha"), 0.45f);
        GL20.glUniform3f(chamsShader.getUniformByName("u_color"), 0.4f, 0.6f, 1.0f);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRender2d(Render2DEvent event) {
        if (framebuffer == null || !binded) return;
        mc.getFramebuffer().bindFramebuffer(true);
        GL20.glUseProgram(chamsShader.getProgramId());
        setupUni();
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        chamsShader.renderShader(event.getResolution());
        binded = false;
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
            if (!entity.isEntityAlive()) continue;
            if (!invisSetting.getValue() && entity.isInvisible()) continue;
            if (ignoreSelfSetting.getValue() && entity == mc.player) continue;
            if (entity == mc.player && mc.gameSettings.thirdPersonView == 0) continue;
            mc.entityRenderer.disableLightmap();
            mc.getRenderManager().setRenderShadow(false);
            mc.getRenderManager().renderEntityStatic(entity, partialTicks, true);
            mc.getRenderManager().setRenderShadow(true);
            binded = true;
        }
    }
}
