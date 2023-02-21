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

public class ChamsModule extends Module {
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

    private final NumberSetting alphaSetting = registerSetting(NumberSetting.builder()
            .name("Alpha")
            .value(0.42)
            .min(0.1)
            .max(1.0)
            .interval(0.1)
            .build()
    );

    private final RefreshingBuffer framebuffer = new RefreshingBuffer(mc.displayWidth, mc.displayHeight, true);
    private final ShaderUtil chamsShader = new ShaderUtil("chams.frag");

    public ChamsModule() {
        super("Chams", Category.OTHER);
    }

    private void setupUniform() {
        Color rainbow = Coloring.getRainbowColor(1, 0, 360);
        GL20.glUniform1i(chamsShader.getUniformByName("u_texture"), 0);
        GL20.glUniform1i(chamsShader.getUniformByName("u_coloring"), 1);
        GL20.glUniform1f(chamsShader.getUniformByName("u_alpha"), alphaSetting.getValue().floatValue());
        GL20.glUniform3f(chamsShader.getUniformByName("u_color"), rainbow.getRed() / 255.0f, rainbow.getGreen() / 255.0f, rainbow.getBlue() / 255.0f);
        GL20.glUniform1f(chamsShader.getUniformByName("u_mixin"), 0.5f);
    }

    @SubscribeEvent
    public void onRender2d(RenderOverlayEvent event) {
        if (!chamsShader.isBounded()) return;
        mc.getFramebuffer().bindFramebuffer(true);
        GL20.glUseProgram(chamsShader.getProgramId());
        setupUniform();
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        chamsShader.renderShader(event.getResolution());
    }

    @SubscribeEvent
    public void onRender3d(RenderWorldEvent event) {
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        renderEntities(event.getPartialTicks());
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);
        mc.entityRenderer.disableLightmap();
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
                chamsShader.setBounded(true);
            }
        }
    }
}
