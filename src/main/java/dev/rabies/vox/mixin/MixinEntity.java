package dev.rabies.vox.mixin;

import dev.rabies.vox.events.game.EntityMoveEvent;
import dev.rabies.vox.modules.ModuleRegistry;
import dev.rabies.vox.modules.modules.HitBoxModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {
    @Inject(method = "move", at = @At("HEAD"))
    public void move(MoverType type, double x, double y, double z, CallbackInfo ci) {
        EntityMoveEvent entityMoveEvent = new EntityMoveEvent();
        MinecraftForge.EVENT_BUS.post(entityMoveEvent);
    }

    @Inject(method = "getCollisionBorderSize", at = @At("HEAD"), cancellable = true)
    public void getCollisionBorderSize(CallbackInfoReturnable<Float> cir) {
        HitBoxModule hitBoxCheat = ModuleRegistry.fromInstance(HitBoxModule.class);
        if (hitBoxCheat != null && !hitBoxCheat.isToggled()) {
            cir.setReturnValue(hitBoxCheat.sizeSetting.getValue().floatValue());
        }
    }
}
