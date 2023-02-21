package dev.rabies.vox.modules.modules;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.ModeSetting;
import dev.rabies.vox.settings.NumberSetting;
import dev.rabies.vox.events.game.PacketEvent;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.misc.ChatHelper;
import dev.rabies.vox.utils.misc.StopWatch;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.RandomUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PingSpooferModule extends Module {
    private final ModeSetting<Mode> modeSetting = registerSetting(ModeSetting.<Mode>builder()
            .name("Mode")
            .value(Mode.Normal)
            .build()
    );

    private final NumberSetting lagInterval = registerSetting(NumberSetting.builder()
        .name("Lag Interval")
        .value(250)
        .min(50)
        .max(2000)
        .interval(1)
        .build()
    );

    private final List<Packet<?>> packetBuffer = new LinkedList<>();
    private final AtomicBoolean endQueue = new AtomicBoolean(true);
    private final StopWatch stopWatch = new StopWatch();

    public PingSpooferModule() {
        super("Ping Spoofer", Category.OTHER);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (!mc.isSingleplayer() && modeSetting.match(Mode.Normal)) {
            int interval = lagInterval.getValue().intValue();
            int randomized = (int) MathHelper.clamp(RandomUtils.nextInt(interval - 400, interval + 100), lagInterval.getMin(), lagInterval.getMax());
            if (!stopWatch.finished(randomized, false) && !endQueue.get()) {
                return;
            }

            endQueue.set(true);
            new Thread(() -> {
                while (!packetBuffer.isEmpty()) {
                    synchronized (packetBuffer) {
                        long sleep = RandomUtils.nextLong(0, 30);
                        if (VoxMod.isDebugMode()) {
                            ChatHelper.info("\2477(Ping\2477) Buff size: " + packetBuffer.size() + ", Sleep: " + sleep);
                        }

                        sendPacketNoEvent(packetBuffer.remove(0));

                        try {
                            Thread.sleep(sleep);
                        } catch (InterruptedException ignore) {
                        }
                    }
                }
                endQueue.set(false);
                stopWatch.reset();
            }).start();
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (!mc.isSingleplayer() && modeSetting.match(Mode.Normal)) {
            Packet<?> packet = event.getPacket();
            if (packet instanceof CPacketKeepAlive || packet instanceof CPacketConfirmTransaction || packet instanceof CPacketPlayerAbilities || packet instanceof CPacketEntityAction) {
                packetBuffer.add(packet);
                event.setCanceled(true);
            }
        }
    }

    public enum Mode {
        Normal, // Real,
    }
}
