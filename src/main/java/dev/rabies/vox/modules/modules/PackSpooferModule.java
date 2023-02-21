package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.events.game.PacketEvent;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PackSpooferModule extends Module {
    public PackSpooferModule() {
        super("Pack Spoofer", Category.OTHER);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (!(event.getPacket() instanceof CPacketResourcePackStatus)) return;
        sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
    }
}
