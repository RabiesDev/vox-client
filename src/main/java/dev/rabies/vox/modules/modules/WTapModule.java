package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.events.game.PacketEvent;
import dev.rabies.vox.events.game.UpdateEvent;
import lombok.Getter;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WTapModule extends Module {
    @Getter
    private boolean tapping;
    private int tick;

    public WTapModule() {
        super("WTap", Category.LEGIT);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (tapping) {
            if (tick == 2) {
                mc.player.setSprinting(true);
                tapping = false;
            }
            tick++;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packetUseEntity = (CPacketUseEntity) event.getPacket();
            if (packetUseEntity.getAction() == CPacketUseEntity.Action.ATTACK && mc.player.isSprinting() && !tapping) {
                mc.player.setSprinting(false);
                tapping = true;
                tick = 0;
            }
        }
    }
}
