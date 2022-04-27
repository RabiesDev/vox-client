package dev.rabies.vox.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class MoveUtil {

    public static boolean isMoving() {
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
        return playerSP.moveForward != 0.0D || playerSP.moveStrafing != 0.0D && !playerSP.isSneaking();
    }
}
