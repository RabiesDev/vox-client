package dev.rabies.vox.mixins;

import dev.rabies.vox.events.PacketEvent;
import dev.rabies.vox.events.VoxEventTiming;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @ModifyVariable(method = "sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), argsOnly = true)
    public Packet<?> modifyPlayPacket(Packet<?> packetIn) {
        PacketEvent packetEvent = new PacketEvent(VoxEventTiming.PRE, packetIn);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        return packetEvent.getPacket();
    }

    @ModifyVariable(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), argsOnly = true)
    public Packet<?> modifyAllPacket(Packet<?> packetIn) {
        PacketEvent packetEvent = new PacketEvent(VoxEventTiming.PRE, packetIn);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        return packetEvent.getPacket();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"))
    public void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo ci) {
        PacketEvent packetEvent = new PacketEvent(VoxEventTiming.POST, p_channelRead0_2_);
        MinecraftForge.EVENT_BUS.post(packetEvent);
    }
}
