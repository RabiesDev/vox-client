package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.NumberSetting;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.events.render.Render3DEvent;
import dev.rabies.vox.utils.ColorUtil;
import dev.rabies.vox.utils.ShaderUtil;
import dev.rabies.vox.utils.misc.ChatUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL20;

import java.awt.*;

public class ChamsCheat extends CheatWrapper {

    private final BoolSetting ignoreSelfSetting = registerBoolSetting("Ignore self", false);
    private final BoolSetting invisSetting = registerBoolSetting("Invisible entity", true);

    private final NumberSetting alphaSetting = registerNumberSetting("Alpha", 0.42f, 0.1f, 1.0f, 0.1f);

    private final ShaderUtil chamsShader = new ShaderUtil("chams_shader.frag");
    private Framebuffer framebuffer;

    public ChamsCheat() {
        super("Chams", Category.OTHER);
    }
    
    private void setupUniform() {
        Color rainbow = ColorUtil.getRainbowColor(0, 360);
        float r = rainbow.getRed() / 255.0f;
        float g = rainbow.getGreen() / 255.0f;
        float b = rainbow.getBlue() / 255.0f;

    	GL20.glUniform1i(chamsShader.getUniformByName("u_texture"), 0);
        GL20.glUniform1i(chamsShader.getUniformByName("u_coloring"), 1);
        GL20.glUniform1f(chamsShader.getUniformByName("u_alpha"), alphaSetting.getValue().floatValue());
        GL20.glUniform3f(chamsShader.getUniformByName("u_color"), r, g, b);
        GL20.glUniform1f(chamsShader.getUniformByName("u_mixin"), 0.5f);
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        if (framebuffer == null || !chamsShader.isBinded()) return;
        mc.getFramebuffer().bindFramebuffer(true);
        GL20.glUseProgram(chamsShader.getProgramId());
        setupUniform();
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        chamsShader.renderShader(event.getResolution());
    }

    @SubscribeEvent
    public void onRender3d(Render3DEvent event) {
        framebuffer = ShaderUtil.createFramebuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        renderEntities(event.getPartialTicks());
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);
        mc.entityRenderer.disableLightmap();
    }

    private void renderEntities(float partialTicks) {
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (!(entity instanceof EntityPlayer)) continue;
            if (!entity.isEntityAlive()) continue;
            if (!invisSetting.getValue() && entity.isInvisible()) continue;
            if (ignoreSelfSetting.getValue() && entity == mc.player) continue;
            if (entity == mc.player && mc.gameSettings.thirdPersonView == 0) continue;
            mc.getRenderManager().setRenderShadow(false);
            mc.getRenderManager().renderEntityStatic(entity, partialTicks, true);
            mc.getRenderManager().setRenderShadow(true);
            chamsShader.setBinded(true);
        }
    }
}
