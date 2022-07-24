package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.ModeSetting;
import dev.rabies.vox.cheats.setting.NumberSetting;
import dev.rabies.vox.events.game.PacketEvent;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.misc.ChatUtil;
import dev.rabies.vox.utils.misc.TimerUtil;
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

public class PingSpooferCheat extends CheatWrapper {

    enum Mode {
        Normal, // Real,
    }

    private final ModeSetting<Mode> modeSetting = registerModeSetting("Mode", Mode.Normal);
    private final NumberSetting lagInterval = registerNumberSetting("Lag Interval", 250, 50, 2000, 1);

    private final TimerUtil timerUtil = new TimerUtil();
    private final List<Packet<?>> packetBuffer = new LinkedList<>();
    private final AtomicBoolean endQueue = new AtomicBoolean(true);

    public PingSpooferCheat() {
        super("Ping Spoofer", Category.OTHER);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (modeSetting.is(Mode.Normal)) {
            int interval = lagInterval.getValue().intValue();
            int randomized = (int) MathHelper.clamp(RandomUtils.nextInt(interval - 400, interval + 100),
                    lagInterval.getMinValue(), lagInterval.getMaxValue());
            if (!timerUtil.delay(randomized) && !endQueue.get()) return;
            endQueue.set(true);
            
            new Thread(() -> {
            	while (!packetBuffer.isEmpty()) {
                    synchronized (packetBuffer) {
                    	long sleep = RandomUtils.nextLong(0, 30);
                    	if (VoxMod.get().isDebugMode()) {
                        	ChatUtil.info("\2477(Ping\2477) Buff size: " + packetBuffer.size() + ", Sleep: " + sleep);
                        }
                    	
                    	sendPacketNoEvent(packetBuffer.remove(0));
                        
                        try {
                        	Thread.sleep(sleep);
                        } catch (InterruptedException ignore) {
    					}
                    }
                }
            	endQueue.set(false);
                timerUtil.reset();
            }).start();
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (mc.isSingleplayer()) return;
        if (modeSetting.is(Mode.Normal)) {
            Packet<?> packet = event.getPacket();
            if (packet instanceof CPacketKeepAlive || packet instanceof CPacketConfirmTransaction ||
            packet instanceof CPacketPlayerAbilities || packet instanceof CPacketEntityAction) {
                packetBuffer.add(packet);
                event.setCanceled(true);
            }
        }
    }
}
