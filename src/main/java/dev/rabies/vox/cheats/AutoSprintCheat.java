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
        if (event.isPost()) return;
        if (!canSprint()) return;
        mc.player.setSprinting(true);
    }

    private boolean canSprint() {
        if (!MoveUtil.isMoving()) return false;
        if (mc.player.isSprinting() || mc.player.isSneaking()) return false;
        if (mc.player.isCreative()) return true;
        return mc.player.getFoodStats().getFoodLevel() > 6;
    }
}
