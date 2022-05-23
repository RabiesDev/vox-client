package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.PlayerUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSprintCheat extends CheatWrapper {

    private final BoolSetting omniSetting = registerBoolSetting("All direction", true);

    public AutoSprintCheat() {
        super("Auto Sprint", Category.LEGIT);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPost() || !canSprint()) return;
        mc.player.setSprinting(true);
    }

    private boolean canSprint() {
        if (!PlayerUtils.isMoving() || mc.player.isSprinting() || mc.player.isSneaking()) return false;
        if (!omniSetting.getValue() && !mc.gameSettings.keyBindForward.isKeyDown()) return false;
        if (WTapCheat.INSTANCE.isTap()) return false;
        return mc.player.isCreative() || mc.player.getFoodStats().getFoodLevel() > 6;
    }
}
