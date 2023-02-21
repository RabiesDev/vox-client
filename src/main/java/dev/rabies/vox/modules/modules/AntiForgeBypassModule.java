package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.events.game.PacketEvent;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class AntiForgeBypassModule extends Module {
    public AntiForgeBypassModule() {
        super("AntiForgeBypass", Category.OTHER);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (!mc.isSingleplayer()) {
            Packet<?> packet = event.getPacket();
            if (packet instanceof FMLProxyPacket) {
                event.setCanceled(true);
            } else if (packet instanceof CPacketCustomPayload) {
                CPacketCustomPayload payloadPacket = (CPacketCustomPayload) event.getPacket();
                if (!payloadPacket.getChannelName().equals("MC|Brand")) return;
                payloadPacket.data = new PacketBuffer(Unpooled.buffer()).writeString("vanilla");
            }
        }
    }
}
