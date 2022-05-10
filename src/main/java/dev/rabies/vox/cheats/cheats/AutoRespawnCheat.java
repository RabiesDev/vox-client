package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.events.game.UpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRespawnCheat extends CheatWrapper {

    public AutoRespawnCheat() {
        super("Auto Respawn", Category.LEGIT);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (!(mc.player.isDead || mc.player.getHealth() <= 0.0D || event.isPre())) return;
        mc.player.respawnPlayer();
    }
}
