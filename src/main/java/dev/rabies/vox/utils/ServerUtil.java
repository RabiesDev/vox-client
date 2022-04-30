package dev.rabies.vox.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ServerUtil {

    public static String getServerIp() {
        ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
        if (serverData == null) {
            return Minecraft.getMinecraft().isSingleplayer() ? "SinglePlayer" : "Unknown";
        }
        return serverData.serverIP;
    }

    public static boolean isTeams(EntityPlayer otherEntity) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean result = false;
        TextFormatting formatting = null;
        TextFormatting formatting2 = null;
        if (otherEntity != null) {
            for (final TextFormatting TextFormatting3 : TextFormatting.values()) {
                if (TextFormatting3 != TextFormatting.RESET) {
                    if (mc.player.getDisplayName().getFormattedText().contains(TextFormatting3.toString()) && formatting == null) {
                        formatting = TextFormatting3;
                    }
                    if (otherEntity.getDisplayName().getFormattedText().contains(TextFormatting3.toString()) && formatting2 == null) {
                        formatting2 = TextFormatting3;
                    }
                }
            }

            if (formatting != null && formatting2 != null) {
                result = (formatting != formatting2);
            } else if (mc.player.getTeam() != null) {
                result = !mc.player.isOnSameTeam(otherEntity);
            } else if (mc.player.inventory.armorInventory.get(3).getItem() instanceof ItemBlock) {
                result = !ItemStack.areItemStacksEqual(
                        mc.player.inventory.armorInventory.get(3),
                        otherEntity.inventory.armorInventory.get(3));
            }
        }
        return result;
    }
}
