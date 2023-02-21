package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.events.game.PacketEvent;
import dev.rabies.vox.utils.PlayerHelper;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KeepSprintModule extends Module {
    public KeepSprintModule() {
        super("Keep Sprint", Category.LEGIT);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof CPacketEntityAction) {
            CPacketEntityAction entityAction = (CPacketEntityAction) event.getPacket();
            if (entityAction.getAction() == CPacketEntityAction.Action.STOP_SPRINTING && PlayerHelper.isMoving()) {
                event.setCanceled(true);
            }
        }
    }
}
