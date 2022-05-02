package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.PacketEvent;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class AntiForgeBypassCheat extends Cheat {

    public AntiForgeBypassCheat() {
        super("AntiForgeBypass", Category.OTHER);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (mc.isSingleplayer()) return;
        Packet<?> packet = event.getPacket();
        if (packet instanceof FMLProxyPacket)
            event.setCanceled(true);

        if (packet instanceof CPacketCustomPayload) {
            CPacketCustomPayload payload = (CPacketCustomPayload) event.getPacket();
            if (!payload.getChannelName().equals("MC|Brand")) return;
            payload.data = new PacketBuffer(Unpooled.buffer()).writeString("vanilla");
        }
    }
}
