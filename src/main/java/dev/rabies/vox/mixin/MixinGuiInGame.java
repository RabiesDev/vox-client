package dev.rabies.vox.mixin;

import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiIngame.class)
public class MixinGuiInGame {
//    @Inject(method = "renderHotbar", at = @At("HEAD"))
//    public void renderHotbar(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
//        MinecraftForge.EVENT_BUS.post(new Render2DEvent(sr, partialTicks));
//    }
}
