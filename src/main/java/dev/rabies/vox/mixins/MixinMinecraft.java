package dev.rabies.vox.mixins;

import dev.rabies.vox.cheats.cheats.FakeFpsCheat;
import dev.rabies.vox.events.game.WindowResizeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    private static int debugFPS;
    @Shadow
    public WorldClient world;

    @Inject(method = "runGameLoop", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/Minecraft;debugFPS:I", shift = At.Shift.AFTER))
    private void runGameLoop(CallbackInfo ci) {
        if (FakeFpsCheat.getInstance() == null) return;
        if (!FakeFpsCheat.getInstance().isEnabled()) return;
        if (world == null) return;
        debugFPS *= RandomUtils.nextInt(20, 35);
    }

    @Inject(method = "resize", at = @At("HEAD"))
    public void resize(int width, int height, CallbackInfo ci) {
        WindowResizeEvent windowResizeEvent = new WindowResizeEvent(width, height);
        MinecraftForge.EVENT_BUS.post(windowResizeEvent);
    }
}
