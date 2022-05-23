package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.NumberSetting;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.events.render.Render3DEvent;
import dev.rabies.vox.utils.render.ColorUtil;
import dev.rabies.vox.utils.render.RefreshFramebuffer;
import dev.rabies.vox.utils.render.ShaderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL20;

import java.awt.*;

public class OutlineEspCheat extends CheatWrapper {

    private final BoolSetting ignoreSelfSetting = registerBoolSetting("Ignore self", false);
    private final BoolSetting invisSetting = registerBoolSetting("Invisible entity", true);

    private final NumberSetting radiusSetting = registerNumberSetting("Radius", 1.0f, 1.0f, 5.0f, 0.5f);

    private final ShaderUtil outlineShader = new ShaderUtil("outline_shader.frag");
    private final RefreshFramebuffer framebuffer = new RefreshFramebuffer(
            mc.displayWidth, mc.displayHeight, true
    );

    public OutlineEspCheat() {
        super("Outline ESP", Category.OTHER);
    }

    private void setupUniform(int direction1, int direction2) {
        Color rainbow = ColorUtil.getRainbowColor(1, 0, 360);
        float r = rainbow.getRed() / 255.0f;
        float g = rainbow.getGreen() / 255.0f;
        float b = rainbow.getBlue() / 255.0f;

        GL20.glUniform1i(outlineShader.getUniformByName("u_texture"), 0);
        GL20.glUniform1f(outlineShader.getUniformByName("u_radius"), radiusSetting.getValue().floatValue());
        GL20.glUniform2f(outlineShader.getUniformByName("u_texelSize"), 1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
        GL20.glUniform2f(outlineShader.getUniformByName("u_direction"), direction1, direction2);
        GL20.glUniform3f(outlineShader.getUniformByName("u_color"), r, g, b);
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        if (!outlineShader.isBinded()) return;
        mc.getFramebuffer().bindFramebuffer(true);
        GL20.glUseProgram(outlineShader.getProgramId());
        setupUniform(0, 1);
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        outlineShader.renderShader(event.getResolution());

        GL20.glUseProgram(outlineShader.getProgramId());
        setupUniform(1, 0);
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        outlineShader.renderShader(event.getResolution());
    }

    @SubscribeEvent
    public void onRender3d(Render3DEvent event) {
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        renderEntities(event.getPartialTicks());
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);
        mc.entityRenderer.disableLightmap();
        GlStateManager.disableLighting();
    }

    private void renderEntities(float partialTicks) {
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (!(entity instanceof EntityPlayer)) continue;
            if (!entity.isEntityAlive()) continue;
            if (!invisSetting.getValue() && entity.isInvisible()) continue;
            if (ignoreSelfSetting.getValue() && entity == mc.player) continue;
            if (entity == mc.player && mc.gameSettings.thirdPersonView == 0) continue;
            mc.getRenderManager().setRenderShadow(false);
            mc.entityRenderer.disableLightmap();
            mc.getRenderManager().renderEntityStatic(entity, partialTicks, true);
            mc.getRenderManager().setRenderShadow(true);
            outlineShader.setBinded(true);
        }
    }
}
