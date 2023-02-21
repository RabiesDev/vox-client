package dev.rabies.vox.mixin;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public class MixinKeyBinding {
    @Shadow
    private boolean pressed;

    @Inject(method = "isKeyDown", at = @At("HEAD"), cancellable = true)
    public void isKeyDown(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.pressed);
    }
}
