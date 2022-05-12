package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.events.game.PacketEvent;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PackSpooferCheat extends CheatWrapper {

    public PackSpooferCheat() {
        super("Pack Spoofer", Category.OTHER);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (!(event.getPacket() instanceof CPacketResourcePackStatus)) return;
        sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
    }
}
