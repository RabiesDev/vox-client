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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.input.Mouse;

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
    	Minecraft mc = Minecraft.getMinecraft();
    	if (mc.objectMouseOver == null) return;
    	if (mc.player.isRowingBoat()) return;
    	switch (mc.objectMouseOver.typeOfHit) {
            case ENTITY:
            	mc.playerController.attackEntity(mc.player, mc.objectMouseOver.entityHit);
                break;
                
            case BLOCK:
                BlockPos blockpos = mc.objectMouseOver.getBlockPos();
                if (!mc.world.isAirBlock(blockpos)) {
                	mc.playerController.clickBlock(blockpos, mc.objectMouseOver.sideHit);
                    break;
                }

            case MISS:
                mc.player.resetCooldown();
                ForgeHooks.onEmptyLeftClick(mc.player);
        }
    	
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    // 0 -> left, 1 -> right, 2 -> middle?
    public static void holdState(int button, boolean state) {
        Mouse.poll();
        MouseEvent mouseEvent = new MouseEvent();
        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, mouseEvent, button, "button");
        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, mouseEvent, state, "buttonstate");
        MinecraftForge.EVENT_BUS.post(mouseEvent);
    }
}
