package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.PacketEvent;
import dev.rabies.vox.events.UpdateEvent;
import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WTapCheat extends Cheat {

    public static WTapCheat INSTANCE;
    @Getter
    private boolean tap;
    private int tick;

    public WTapCheat() {
        super("WTap", Category.RAGE);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (!tap) return;
        if (tick == 2) {
            mc.player.setSprinting(true);
            tap = false;
        }
        tick++;
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (event.isIn()) return;
        Packet<?> packet = event.getPacket();
        if (!(packet instanceof CPacketUseEntity)) return;
        CPacketUseEntity packetUseEntity = (CPacketUseEntity) packet;
        if (packetUseEntity.getAction() != CPacketUseEntity.Action.ATTACK) return;
        if (mc.player.getFoodStats().getFoodLevel() <= 6) return;
        if (!mc.player.isSprinting()) return;
        if (tap) return;
        mc.player.setSprinting(false);
        tick = 0;
        tap = true;
    }
}
