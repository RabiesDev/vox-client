package dev.rabies.vox.utils;

import dev.rabies.vox.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils {

    public static void info(Object content) {
        Minecraft.getMinecraft().ingameGUI.addChatMessage(
                ChatType.SYSTEM,
                new TextComponentString(Constants.PREFIX.concat(content.toString()))
        );
    }

    public static void send(Object content) {
        Minecraft.getMinecraft().player.sendChatMessage(content.toString());
    }
}
