package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.BooleanSetting;
import dev.rabies.vox.settings.NumberSetting;
import dev.rabies.vox.events.game.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class VelocityModule extends Module {
    private final BooleanSetting waterSetting = registerSetting(BooleanSetting.builder()
            .name("No water")
            .value(true)
            .build()
    );

    private final BooleanSetting lavaSetting = registerSetting(BooleanSetting.builder()
            .name("No lava")
            .value(true)
            .build()
    );

    private final BooleanSetting ladderSetting = registerSetting(BooleanSetting.builder()
            .name("No ladder")
            .value(true)
            .build()
    );

    private final NumberSetting chanceSetting = registerSetting(NumberSetting.builder()
            .name("Chance")
            .value(80)
            .min(10)
            .max(100)
            .interval(1)
            .build()
    );

    private final NumberSetting horizontalSetting = registerSetting(NumberSetting.builder()
            .name("Horizontal")
            .value(60)
            .min(0)
            .max(100)
            .interval(1)
            .build()
    );

    private final NumberSetting verticalSetting = registerSetting(NumberSetting.builder()
            .name("Vertical")
            .value(70)
            .min(0)
            .max(100)
            .interval(1)
            .build()
    );

    public VelocityModule() {
        super("Velocity", Category.LEGIT);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
    	if (mc.player == null || (waterSetting.getValue() && mc.player.isInWater()) || (lavaSetting.getValue() && mc.player.isInLava()) || (ladderSetting.getValue() && mc.player.isOnLadder())) {
            return;
        }

        Random random = new Random();
        double horizontal = horizontalSetting.getValue();
        double vertical = verticalSetting.getValue();

        Packet<?> packet = event.getPacket();
        if (packet instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity velocityPacket = (SPacketEntityVelocity) packet;
            if (MathHelper.getInt(random, 0, 100) > 100 - chanceSetting.getValue()) {
                if (horizontal == 0 && vertical == 0) {
                    event.setCanceled(true);
                    return;
                }

                double nextDouble = random.nextDouble();
                double motionX = velocityPacket.getMotionX();
                double motionY = velocityPacket.getMotionY();
                double motionZ = velocityPacket.getMotionZ();
                double smartHorizontal = horizontal + (horizontal + 5 - horizontal) * nextDouble;
                if (smartHorizontal >= horizontalSetting.getMax()) {
                    smartHorizontal = horizontalSetting.getMax();
                }

                double smartVertical = vertical + (vertical + 5 - vertical) * nextDouble;
                if (smartVertical >= verticalSetting.getMax()) {
                    smartVertical = verticalSetting.getMax();
                }

                velocityPacket.motionX = (int) ((motionX / 8000.0 - mc.player.motionX) * smartHorizontal / 100);
                velocityPacket.motionY = (int) ((motionY / 8000.0 - mc.player.motionY) * smartVertical / 100);
                velocityPacket.motionZ = (int) ((motionZ / 8000.0 - mc.player.motionZ) * smartHorizontal / 100);
            }
        } else if (packet instanceof SPacketExplosion) {
            SPacketExplosion explosionPacket = (SPacketExplosion) packet;
            if (MathHelper.getInt(random, 0, 100) > 100 - chanceSetting.getValue()) {
                if (horizontal == 0 && vertical == 0) {
                    event.setCanceled(true);
                    return;
                }

                double nextDouble = random.nextDouble();
                double motionX = explosionPacket.getMotionX();
                double motionY = explosionPacket.getMotionY();
                double motionZ = explosionPacket.getMotionZ();
                double smartHorizontal = horizontal + (horizontal + 5 - horizontal) * nextDouble;
                if (smartHorizontal >= horizontalSetting.getMax()) {
                    smartHorizontal = horizontalSetting.getMax();
                }

                double smartVertical = vertical + (vertical + 5 - vertical) * nextDouble;
                if (smartVertical >= verticalSetting.getMax()) {
                    smartVertical = verticalSetting.getMax();
                }

                explosionPacket.motionX = (float) ((motionX / 8000.0 - mc.player.motionX) * smartHorizontal / 100);
                explosionPacket.motionY = (float) ((motionY / 8000.0 - mc.player.motionY) * smartVertical / 100);
                explosionPacket.motionZ = (float) ((motionZ / 8000.0 - mc.player.motionZ) * smartHorizontal / 100);
            }
        }
    }
}
