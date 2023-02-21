package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.events.game.EntityMoveEvent;
import dev.rabies.vox.events.game.PacketEvent;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.PlayerHelper;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FreecamModule extends Module {
    private double lastX, lastY, lastZ;
    private float lastYaw, lastPitch;
    private boolean lastGround;

    public FreecamModule() {
        super("Freecam", Category.RAGE);
    }

    @Override
    public void onEnable() {
        if (isToggleable()) {
            return;
        }

        lastX = mc.player.posX;
        lastY = mc.player.posY;
        lastZ = mc.player.posZ;
        lastYaw = mc.player.rotationYaw;
        lastPitch = mc.player.rotationPitch;
        lastGround = mc.player.onGround;
        mc.player.noClip = true;
        spawnFakePlayer();
    }

    @Override
    public void onDisable() {
        mc.player.motionX = 0;
        mc.player.motionY = 0;
        mc.player.motionZ = 0;
        mc.player.noClip = false;
        mc.player.capabilities.isFlying = false;
        mc.world.removeEntityFromWorld(-1337);
        mc.player.setPositionAndRotation(lastX, lastY, lastZ, lastYaw, lastPitch);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        double boost = 0.6;
        if (mc.gameSettings.keyBindSprint.isKeyDown()) {
            boost *= 2;
        }

        mc.player.setVelocity(0, 0, 0);
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY = boost / 1.8;
        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY = -boost / 1.8;
        }
        PlayerHelper.setMotion(boost);
    }

    @SubscribeEvent
    public void onEntityMove(EntityMoveEvent event) {
        mc.player.noClip = true;
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (!(event.getPacket() instanceof CPacketPlayer)) return;
        sendPacketNoEvent(new CPacketPlayer.PositionRotation(lastX, lastY, lastZ, lastYaw, lastPitch, lastGround));
        event.setCanceled(true);
    }

    private void spawnFakePlayer() {
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        fakePlayer.inventory = mc.player.inventory;
        fakePlayer.inventoryContainer = mc.player.inventoryContainer;
        fakePlayer.rotationYawHead = lastYaw;
        fakePlayer.setPositionAndRotation(lastX, lastY, lastZ, lastYaw, lastPitch);
        mc.world.addEntityToWorld(-1337, fakePlayer);
    }
}
