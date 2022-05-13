package dev.rabies.vox.mixins;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.events.game.PacketEvent;
import dev.rabies.vox.events.VoxEventTiming;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V",
            at = @At("HEAD"), cancellable = true)
    public void sendPacket(Packet<?> packetIn, GenericFutureListener<? extends Future<? super Void>> p_179288_2_,
                                 GenericFutureListener<? extends Future<? super Void>>[] p_179288_3_, CallbackInfo ci) {
        if (VoxMod.get().isIgnorePacket()) return;
        PacketEvent packetEvent = new PacketEvent(VoxEventTiming.PRE, packetIn);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        if (packetEvent.isCanceled())
            ci.cancel();
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void sendPacket(Packet<?> packetIn, CallbackInfo ci) {
        if (VoxMod.get().isIgnorePacket()) return;
        PacketEvent packetEvent = new PacketEvent(VoxEventTiming.PRE, packetIn);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        if (packetEvent.isCanceled())
            ci.cancel();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo ci) {
        PacketEvent packetEvent = new PacketEvent(VoxEventTiming.POST, p_channelRead0_2_);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        if (packetEvent.isCanceled())
            ci.cancel();
    }
}
