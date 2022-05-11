package dev.rabies.vox.mixins;

import dev.rabies.vox.events.render.Render2DEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSubtitleOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSubtitleOverlay.class)
public class MixinGuiSubtitleOverlay {

    @Inject(method = "renderSubtitles", at = @At("HEAD"))
    public void renderSubtitles(ScaledResolution resolution, CallbackInfo ci) {
        Render2DEvent render2DEvent = new Render2DEvent(resolution, Minecraft.getMinecraft().getRenderPartialTicks());
        MinecraftForge.EVENT_BUS.post(render2DEvent);
        GlStateManager.resetColor();
    }
}
