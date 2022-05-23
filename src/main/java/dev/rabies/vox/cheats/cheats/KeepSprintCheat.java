package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.events.game.PacketEvent;
import dev.rabies.vox.utils.PlayerUtils;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KeepSprintCheat extends CheatWrapper {

    public KeepSprintCheat() {
        super("Keep Sprint", Category.LEGIT);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (event.isOut() || !(event.getPacket() instanceof CPacketEntityAction)) return;
        CPacketEntityAction entityAction = (CPacketEntityAction) event.getPacket();
        if (entityAction.getAction() != CPacketEntityAction.Action.STOP_SPRINTING) return;
        if (!PlayerUtils.isMoving()) return;
        event.setCanceled(true);
    }
}
