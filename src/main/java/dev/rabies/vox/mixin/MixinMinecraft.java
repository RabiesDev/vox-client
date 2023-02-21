package dev.rabies.vox.mixin;

import dev.rabies.vox.events.game.WindowResizeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow
    private static int debugFPS;
    @Shadow
    public WorldClient world;

    @Inject(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;debugFPS:I", shift = At.Shift.AFTER))
    private void runGameLoop(CallbackInfo ci) {
        if (world != null) {
            debugFPS *= ThreadLocalRandom.current().nextGaussian();
        }
    }

    @Inject(method = "resize", at = @At("HEAD"))
    public void resize(int width, int height, CallbackInfo ci) {
        WindowResizeEvent windowResizeEvent = new WindowResizeEvent(width, height);
        MinecraftForge.EVENT_BUS.post(windowResizeEvent);
    }
}
