package dev.rabies.vox.utils;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class PlayerUtils {

    public static boolean isMoving() {
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
        return playerSP.moveForward != 0.0D || playerSP.moveStrafing != 0.0D && !playerSP.isSneaking();
    }

    public static boolean isBlockUnder() {
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
        for (int i = (int) (playerSP.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(playerSP.posX, i, playerSP.posZ);
            if (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    public static void attack(Entity target, boolean legitReach) {
        Minecraft mc = Minecraft.getMinecraft();
        PlayerControllerMP pc = mc.playerController;
        // 🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨🤨
        if (mc.player.getDistance(target) > pc.getBlockReachDistance() &&
                legitReach) return;
        pc.attackEntity(mc.player, target);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static void legitAttack() {
        // bruh no im retadedo
        KeyBinding attackKey = Minecraft.getMinecraft().gameSettings.keyBindAttack;
        KeyBinding.setKeyBindState(attackKey.getKeyCode(), true);
//        KeyBinding.onTick(attackKey.getKeyCode());
        KeyBinding.resetKeyBindingArrayAndHash();
    }

    public static void holdState(boolean state) {
        // ok raven moment 🤨
        MouseEvent mouseEvent = new MouseEvent();
        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, mouseEvent, 1, "button");
        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, mouseEvent, state, "buttonstate");
        MinecraftForge.EVENT_BUS.post(mouseEvent);
    }
}
