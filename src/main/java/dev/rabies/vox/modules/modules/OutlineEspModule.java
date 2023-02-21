package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.BooleanSetting;
import dev.rabies.vox.settings.NumberSetting;
import dev.rabies.vox.events.render.RenderOverlayEvent;
import dev.rabies.vox.events.render.RenderWorldEvent;
import dev.rabies.vox.utils.render.Coloring;
import dev.rabies.vox.utils.render.RefreshingBuffer;
import dev.rabies.vox.utils.render.ShaderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL20;

import java.awt.*;

public class OutlineEspModule extends Module {
    private final BooleanSetting ignoreSelfSetting = registerSetting(BooleanSetting.builder()
            .name("Ignore Self")
            .value(true)
            .build()
    );

    private final BooleanSetting ignoreInvisibleSetting = registerSetting(BooleanSetting.builder()
            .name("Ignore Invisible")
            .value(true)
            .build()
    );

    private final NumberSetting radiusSetting = registerSetting(NumberSetting.builder()
            .name("Radius")
            .value(1.0)
            .min(1.0)
            .max(5.0)
            .interval(0.5)
            .build()
    );

    private final ShaderUtil outlineShader = new ShaderUtil("outline.frag");
    private final RefreshingBuffer framebuffer = new RefreshingBuffer(
            mc.displayWidth, mc.displayHeight, true
    );

    public OutlineEspModule() {
        super("Outline ESP", Category.OTHER);
    }

    private void setupUniform(int direction1, int direction2) {
        Color rainbow = Coloring.getRainbowColor(1, 0, 360);
        GL20.glUniform1i(outlineShader.getUniformByName("u_texture"), 0);
        GL20.glUniform1f(outlineShader.getUniformByName("u_radius"), radiusSetting.getValue().floatValue());
        GL20.glUniform2f(outlineShader.getUniformByName("u_texelSize"), 1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
        GL20.glUniform2f(outlineShader.getUniformByName("u_direction"), direction1, direction2);
        GL20.glUniform3f(outlineShader.getUniformByName("u_color"), rainbow.getRed() / 255.0f, rainbow.getGreen() / 255.0f, rainbow.getBlue() / 255.0f);
    }

    @SubscribeEvent
    public void onRender2d(RenderOverlayEvent event) {
        if (!outlineShader.isBounded()) return;
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
    public void onRender3d(RenderWorldEvent event) {
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
            if (entity instanceof EntityPlayer && entity.isEntityAlive()) {
                if ((ignoreSelfSetting.getValue() && entity == mc.player) || (!ignoreInvisibleSetting.getValue() && entity.isInvisible()) || (entity == mc.player && mc.gameSettings.thirdPersonView == 0)) {
                    continue;
                }

                mc.entityRenderer.disableLightmap();
                mc.getRenderManager().setRenderShadow(false);
                mc.getRenderManager().renderEntityStatic(entity, partialTicks, true);
                outlineShader.setBounded(true);
            }
        }
    }
}
