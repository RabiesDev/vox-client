package dev.rabies.vox.utils;

import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.input.Mouse;

public class PlayerHelper {
    public static boolean isMoving() {
        EntityPlayerSP localPlayer = Minecraft.getMinecraft().player;
        return localPlayer.moveForward != 0 || localPlayer.moveStrafing != 0 && !localPlayer.isSneaking();
    }

    public static boolean isBlockUnder() {
        EntityPlayerSP localPlayer = Minecraft.getMinecraft().player;
        for (int i = (int) (localPlayer.posY - 1); i > 0; --i) {
            BlockPos pos = new BlockPos(localPlayer.posX, i, localPlayer.posZ);
            if (!(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }

    public static void attack(Entity target, boolean legitReach) {
        Minecraft mc = Minecraft.getMinecraft();
        PlayerControllerMP controller = mc.playerController;
        // 🤨
        if (mc.player.getDistance(target) > controller.getBlockReachDistance() && legitReach) {
            return;
        }

        controller.attackEntity(mc.player, target);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static void legitAttack() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.objectMouseOver == null || mc.player.isRowingBoat()) {
            return;
        }

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

    public static void legitClick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player.isRowingBoat()) return;
        for (EnumHand hand : EnumHand.values()) {
            ItemStack heldItem = mc.player.getHeldItem(hand);
            if (mc.objectMouseOver != null) {
                switch (mc.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        if (mc.playerController.interactWithEntity(mc.player, mc.objectMouseOver.entityHit, mc.objectMouseOver, hand) == EnumActionResult.SUCCESS) {
                            return;
                        }

                        if (mc.playerController.interactWithEntity(mc.player, mc.objectMouseOver.entityHit, hand) == EnumActionResult.SUCCESS) {
                            return;
                        }
                        break;

                    case BLOCK:
                        BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                        if (mc.world.getBlockState(blockPos).getMaterial() != Material.AIR) {
                            int i = heldItem.getCount();
                            EnumActionResult actionResult = mc.playerController.processRightClickBlock(mc.player, mc.world, blockPos, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec, hand);
                            if (actionResult == EnumActionResult.SUCCESS) {
                                mc.player.swingArm(hand);
                                if (!heldItem.isEmpty() && (heldItem.getCount() != i || mc.playerController.isInCreativeMode())) {
                                    mc.entityRenderer.itemRenderer.resetEquippedProgress(hand);
                                }
                                return;
                            }
                        }
                }
            }

            if (heldItem.isEmpty() && (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit == RayTraceResult.Type.MISS)) {
                ForgeHooks.onEmptyClick(mc.player, hand);
            }

            if (!heldItem.isEmpty() && mc.playerController.processRightClick(mc.player, mc.world, hand) == EnumActionResult.SUCCESS) {
                mc.entityRenderer.itemRenderer.resetEquippedProgress(hand);
                return;
            }
        }
    }

    // 0 -> left
    // 1 -> right
    // 2 -> middle
    public static void holdState(int button, boolean state) {
        Mouse.poll();
        MouseEvent mouseEvent = new MouseEvent();
        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, mouseEvent, button, "button");
        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, mouseEvent, state, "buttonstate");
        MinecraftForge.EVENT_BUS.post(mouseEvent);
    }

    public static void setMotion(double speed) {
        EntityPlayerSP localPlayer = Minecraft.getMinecraft().player;
        double forward = localPlayer.moveForward;
        double strafe = localPlayer.moveStrafing;
        float yaw = localPlayer.rotationYaw;
        if (isMoving()) {
            Vec3d motion = new Vec3d(-forward, 0, strafe).normalize().rotateYaw((float) Math.toRadians(90 - yaw)).scale(speed);
            localPlayer.motionX = motion.x;
            localPlayer.motionZ = motion.z;
        } else {
            localPlayer.motionX = 0;
            localPlayer.motionZ = 0;
        }
    }
}
