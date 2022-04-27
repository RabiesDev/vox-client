package dev.rabies.vox.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

//    @Inject(method = "init", at = @At("HEAD"))
//    public void init(CallbackInfo ci) {
//        System.out.println("mixin test");
//    }
}
