package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.events.game.EntityMoveEvent;
import dev.rabies.vox.events.game.PacketEvent;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.PlayerUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class FreecamCheat extends CheatWrapper {

    private double posX, posY, posZ;
    private float yaw, pitch;
    private boolean onGround;

    public FreecamCheat() {
        super("Freecam", Category.RAGE);
    }

    @Override
    public void onEnable() {
    	toggleableCheck();
        posX = mc.player.posX;
        posY = mc.player.posY;
        posZ = mc.player.posZ;
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
        onGround = mc.player.onGround;
        mc.player.noClip = true;
        spawn();
    }

    @Override
    public void onDisable() {
    	mc.player.motionX = 0;
    	mc.player.motionY = 0;
    	mc.player.motionZ = 0;
    	mc.player.fallDistance = 0;
        mc.player.noClip = false;
        mc.player.capabilities.isFlying = false;
        mc.world.removeEntityFromWorld(-1337);
        mc.player.setPositionAndRotation(posX, posY, posZ, yaw, pitch);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        double motion = 0.6d;
        PlayerUtils.setMotion(motion);
        mc.player.motionY = 0.0D;
        if (!mc.inGameHasFocus) return;
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
            mc.player.motionY = motion;
        } else if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
            mc.player.motionY = -motion;
        }
    }

    @SubscribeEvent
    public void onEntityMove(EntityMoveEvent event) {
        mc.player.noClip = true;
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
    	if (!(event.getPacket() instanceof CPacketPlayer)) return;
    	event.setPacket(new CPacketPlayer.PositionRotation(posX, posY, posZ, yaw, pitch, onGround));
    }

    private void spawn() {
        EntityOtherPlayerMP playerMP = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        playerMP.inventory = mc.player.inventory;
        playerMP.inventoryContainer = mc.player.inventoryContainer;
        playerMP.rotationYawHead = yaw;
        playerMP.setPositionAndRotation(posX, posY, posZ, yaw, pitch);
        mc.world.addEntityToWorld(-1337, playerMP);
    }
}
