package dev.rabies.vox.mixin;

import dev.rabies.vox.commands.CommandRegistry;
import dev.rabies.vox.events.EventTiming;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.misc.ChatHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    public void onPreUpdateWalkingPlayer(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new UpdateEvent(EventTiming.PRE));
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    public void onPostUpdateWalkingPlayer(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new UpdateEvent(EventTiming.POST));
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        if (message.startsWith(":")) {
            ci.cancel();

            try {
                CommandRegistry.getDispatcher().execute(message.substring(1), Minecraft.getMinecraft().player);
            } catch (Exception e) {
                ChatHelper.error(e.getMessage());
            }
        }
    }
}
