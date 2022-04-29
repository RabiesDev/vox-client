package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.events.UpdateEvent;
import dev.rabies.vox.utils.PlayerUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSprintCheat extends Cheat {

    private final BoolSetting omniSetting = registerBoolSetting("All direction", true);

    public AutoSprintCheat() {
        super("AutoSprint", Category.LEGIT);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPost()) return;
        if (!canSprint()) return;
        mc.player.setSprinting(true);
    }

    private boolean canSprint() {
        if (!PlayerUtils.isMoving()) return false;
        if (mc.player.isSprinting() || mc.player.isSneaking()) return false;
        if (!omniSetting.getValue() && !mc.gameSettings.keyBindForward.isKeyDown()) return false;
        if (mc.player.isCreative()) return true;
        return mc.player.getFoodStats().getFoodLevel() > 6;
    }
}
