package dev.rabies.vox.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtil {

    public static void send(Packet<?> packetIn) {
        Minecraft.getMinecraft().player.connection
                .sendPacket(packetIn);
    }

//    public static void sendNoEvent(Packet<?> packetIn) {
//        Minecraft.getMinecraft().player.connection
//                .sendPacket(packetIn);
//    }
}
