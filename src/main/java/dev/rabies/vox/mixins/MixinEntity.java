package dev.rabies.vox.mixins;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.cheats.HitBoxCheat;
import dev.rabies.vox.events.game.EntityMoveEvent;
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
        HitBoxCheat hitBoxCheat = (HitBoxCheat) VoxMod.get().getCheatByName("hitbox");
        if (hitBoxCheat == null) return;
        cir.setReturnValue(1.0f + hitBoxCheat.sizeSetting.getValue().floatValue());
    }
}
