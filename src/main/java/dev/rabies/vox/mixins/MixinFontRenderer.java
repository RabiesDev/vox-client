package dev.rabies.vox.mixins;

import dev.rabies.vox.events.VoxEventTiming;
import dev.rabies.vox.events.render.RenderTextEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    @ModifyVariable(method = "renderString", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String renderString(final String string) {
        RenderTextEvent renderTextEvent = new RenderTextEvent(VoxEventTiming.PRE, string);
        MinecraftForge.EVENT_BUS.post(renderTextEvent);
        return renderTextEvent.getString();
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String getStringWidth(final String string) {
        RenderTextEvent renderTextEvent = new RenderTextEvent(VoxEventTiming.PRE, string);
        MinecraftForge.EVENT_BUS.post(renderTextEvent);
        return renderTextEvent.getString();
    }
}
