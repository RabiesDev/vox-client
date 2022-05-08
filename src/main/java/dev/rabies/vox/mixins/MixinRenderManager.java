package dev.rabies.vox.mixins;

import dev.rabies.vox.events.VoxEventTiming;
import dev.rabies.vox.events.render.RenderEntityEvent;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager {

    @Shadow private double renderPosX;
    @Shadow private double renderPosY;
    @Shadow private double renderPosZ;

    @Shadow
    public abstract void renderEntity(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_);

    @Inject(method = "renderEntityStatic", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V",
            shift = At.Shift.BEFORE))
    public void preRenderEntity(Entity entityIn, float partialTicks, boolean p_188388_3_, CallbackInfo ci) {
        RenderEntityEvent renderEntityEvent = new RenderEntityEvent(VoxEventTiming.PRE, entityIn, event -> {
            double x = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
            double y = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
            double z = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
            float yaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
            renderEntity(entityIn, x - this.renderPosX, y - this.renderPosY, z - this.renderPosZ, yaw, partialTicks, p_188388_3_);
        });
        MinecraftForge.EVENT_BUS.post(renderEntityEvent);
    }

    @Inject(method = "renderEntityStatic", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V",
            shift = At.Shift.AFTER))
    public void postRenderEntity(Entity entityIn, float partialTicks, boolean p_188388_3_, CallbackInfo ci) {
        RenderEntityEvent renderEntityEvent = new RenderEntityEvent(VoxEventTiming.POST, entityIn, event -> {
            double x = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
            double y = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
            double z = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
            float yaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
            renderEntity(entityIn, x - this.renderPosX, y - this.renderPosY, z - this.renderPosZ, yaw, partialTicks, p_188388_3_);
        });
        MinecraftForge.EVENT_BUS.post(renderEntityEvent);
    }
}
