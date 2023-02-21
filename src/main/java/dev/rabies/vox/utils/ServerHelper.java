package dev.rabies.vox.utils;

import dev.rabies.vox.friends.FriendRegistry;
import joptsimple.internal.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

public class ServerHelper {
    public static Color getTeamColor(Entity entityIn) {
        Color color = Color.WHITE;
        ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam) entityIn.getTeam();
        if (scoreplayerteam != null) {
            String s = FontRenderer.getFormatFromString(scoreplayerteam.getPrefix());
            if (!Strings.isNullOrEmpty(s) && s.length() >= 2) {
                color = new Color(Minecraft.getMinecraft().fontRenderer.getColorCode(s.charAt(1)));
            }
        }
        return color;
    }

    public static boolean isTeammate(EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean result = false;
        TextFormatting formatting = null;
        TextFormatting formatting2 = null;
        if (player != null) {
            for (TextFormatting textFormatting : TextFormatting.values()) {
                if (textFormatting != TextFormatting.RESET) {
                    if (mc.player.getDisplayName().getFormattedText().contains(textFormatting.toString()) && formatting == null) {
                        formatting = textFormatting;
                    }
                    if (player.getDisplayName().getFormattedText().contains(textFormatting.toString()) && formatting2 == null) {
                        formatting2 = textFormatting;
                    }
                }
            }

            if (formatting != null && formatting2 != null) {
                result = (formatting == formatting2);
            } else if (mc.player.getTeam() != null) {
                result = mc.player.isOnSameTeam(player);
            } else if (mc.player.inventory.armorInventory.get(3).getItem() instanceof ItemBlock) {
                result = ItemStack.areItemStacksEqual(mc.player.inventory.armorInventory.get(3), player.inventory.armorInventory.get(3));
            }
        }
        return result;
    }

    public static boolean isFriend(EntityPlayer player) {
        return FriendRegistry.getFriends().stream().anyMatch(ign -> ign.equals(player.getName()));
    }
}
