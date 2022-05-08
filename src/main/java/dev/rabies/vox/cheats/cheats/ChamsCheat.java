package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.render.RenderEntityEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChamsCheat extends Cheat {

    public ChamsCheat() {
        super("Chams", Category.OTHER);
    }

    @SubscribeEvent
    public void onRenderEntity(RenderEntityEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) return;
        if (event.isPre()) {
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.disableLighting();
            mc.entityRenderer.disableLightmap();
            mc.getRenderManager().setRenderShadow(false);
        } else {
            GlStateManager.enableDepth();
            GlStateManager.enableLighting();
            mc.getRenderManager().setRenderShadow(true);
            event.getCallback().render(event);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }
    }
}
