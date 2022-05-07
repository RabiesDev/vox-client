package dev.rabies.vox.cheats.cheats;

import org.lwjgl.opengl.GL11;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.render.RenderModelEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChamsCheat extends Cheat {

    public ChamsCheat() {
        super("Chams", Category.OTHER);
    }

    @SubscribeEvent
    public void onRenderModel(RenderModelEvent event) {
        if (!(event.getLivingBase() instanceof EntityPlayer)) return;
        if (event.isPre()) {
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.enableAlpha();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        } else {
        	GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            event.getCallback().render(event);
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
