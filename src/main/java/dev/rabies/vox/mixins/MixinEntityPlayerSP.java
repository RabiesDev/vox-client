package dev.rabies.vox.mixins;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.commands.Command;
import dev.rabies.vox.events.UpdateEvent;
import dev.rabies.vox.events.VoxEventTiming;
import dev.rabies.vox.utils.ChatUtils;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    public void onPreUpdateWalkingPlayer(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new UpdateEvent(VoxEventTiming.PRE));
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    public void onPostUpdateWalkingPlayer(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new UpdateEvent(VoxEventTiming.POST));
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        if (!message.startsWith(":")) return;
        String[] commandSplit = message.split(" ");
        Command command = VoxMod.get().getCommands().stream()
                .filter(it -> {
                    String first = commandSplit[0].substring(1);
                    return it.getName().equalsIgnoreCase(first) ||
                            Arrays.stream(it.getAliases()).anyMatch(s -> s.equalsIgnoreCase(first));
                }).findFirst().orElse(null);
        if (command == null) {
            ChatUtils.error("No command found");
            ChatUtils.error("Try running :help");
        } else {
            command.execute(Arrays.copyOfRange(commandSplit, 1, commandSplit.length));
        }
        ci.cancel();
    }
}
