package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.events.game.UpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRespawnModule extends Module {
    public AutoRespawnModule() {
        super("Auto Respawn", Category.LEGIT);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (mc.player.isDead && mc.player.getHealth() <= 0) {
            mc.player.respawnPlayer();
        }
    }
}
