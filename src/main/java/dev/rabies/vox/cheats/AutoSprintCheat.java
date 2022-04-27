package dev.rabies.vox.cheats;

import dev.rabies.vox.events.UpdateEvent;
import dev.rabies.vox.utils.MoveUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSprintCheat extends Cheat {

    public AutoSprintCheat() {
        super("AutoSprint");
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (!canSprint()) return;
        player.setSprinting(true);
    }

    private boolean canSprint() {
        if (!MoveUtil.isMoving()) return false;
        if (player.isSprinting() || player.isSneaking()) return false;
        if (player.isCreative()) return true;
        return player.getFoodStats().getFoodLevel() > 6;
    }
}
