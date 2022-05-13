package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.NumberSetting;
import dev.rabies.vox.events.game.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class VelocityCheat extends CheatWrapper {

    private final BoolSetting waterSetting = registerBoolSetting("No water", true);
    private final BoolSetting lavaSetting = registerBoolSetting("No lava", true);
    private final BoolSetting ladderSetting = registerBoolSetting("No ladder", true);

    private final NumberSetting chanceSetting = registerNumberSetting("Chance", 80, 10, 100, 1);
    private final NumberSetting horizontalSetting = registerNumberSetting("Horizontal", 60, 0, 100, 1);
    private final NumberSetting verticalSetting = registerNumberSetting("Vertical", 70, 0, 100, 1);

    public VelocityCheat() {
        super("Velocity", Category.LEGIT);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (waterSetting.getValue() && mc.player.isInWater()) return;
        if (lavaSetting.getValue() && mc.player.isInLava()) return;
        if (ladderSetting.getValue() && mc.player.isOnLadder()) return;

        Random random = new Random();
        double horizontal = horizontalSetting.getValue();
        double vertical = verticalSetting.getValue();

        Packet<?> packet = event.getPacket();
        if (packet instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity velocity = (SPacketEntityVelocity) packet;
            if (MathHelper.getInt(random, 0, 100) > 100.0 - chanceSetting.getValue()) {
                if (horizontal == 0.0 && vertical == 0.0) {
                    event.setCanceled(true);
                    return;
                }

                double nextDouble = random.nextDouble();
                double motionX = velocity.getMotionX();
                double motionY = velocity.getMotionY();
                double motionZ = velocity.getMotionZ();
                double smartHorizontal = horizontal + (horizontal + 5.0 - horizontal) * nextDouble;
                if (smartHorizontal >= horizontalSetting.getMaxValue())
                    smartHorizontal = horizontalSetting.getMaxValue();

                double smartVertical = vertical + (vertical + 5.0 - vertical) * nextDouble;
                if (smartVertical >= verticalSetting.getMaxValue())
                    smartVertical = verticalSetting.getMaxValue();

                velocity.motionX = (int) format(motionX, smartHorizontal / 100.0);
                velocity.motionY = (int) format(motionY, smartVertical / 100.0);
                velocity.motionZ = (int) format(motionZ, smartHorizontal / 100.0);
            }
        } else if (packet instanceof SPacketExplosion) {
            SPacketExplosion explosion = (SPacketExplosion) packet;
            if (MathHelper.getInt(random, 0, 100) > 100.0 - chanceSetting.getValue()) {
                if (horizontal == 0.0 && vertical == 0.0) {
                    event.setCanceled(true);
                    return;
                }

                double nextDouble = random.nextDouble();
                double motionX = explosion.getMotionX();
                double motionY = explosion.getMotionY();
                double motionZ = explosion.getMotionZ();
                double smartHorizontal = horizontal + (horizontal + 5.0 - horizontal) * nextDouble;
                if (smartHorizontal >= horizontalSetting.getMaxValue())
                    smartHorizontal = horizontalSetting.getMaxValue();

                double smartVertical = vertical + (vertical + 5.0 - vertical) * nextDouble;
                if (smartVertical >= verticalSetting.getMaxValue())
                    smartVertical = verticalSetting.getMaxValue();

                explosion.motionX = (int) format(motionX, smartHorizontal / 100.0);
                explosion.motionY = (int) format(motionY, smartVertical / 100.0);
                explosion.motionZ = (int) format(motionZ, smartHorizontal / 100.0);
            }
        }
    }

    private double format(double first, double second) {
        String absStr = Double.toString(Math.abs(first));
        int len = absStr.length() - absStr.indexOf(absStr.contains(",") ? "," : ".") - 1;
        String replaced = String.valueOf(first * second).replaceAll(",", ".");
        if (replaced.contains("E")) return Double.parseDouble(replaced);
        if (replaced.contains(".")) return Double.parseDouble(replaced
                .substring(0, Math.min(replaced.indexOf(46) + len + 1, replaced.length())));
        return Double.parseDouble(replaced);
    }
}
