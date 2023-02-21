package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.modules.ModuleRegistry;
import dev.rabies.vox.settings.BooleanSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.PlayerHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSprintModule extends Module {
    private final BooleanSetting allDirectionSetting = registerSetting(BooleanSetting.builder()
            .name("All Direction")
            .value(true)
            .build()
    );

    public AutoSprintModule() {
        super("Auto Sprint", Category.LEGIT);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() && shouldSprint() && (allDirectionSetting.getValue() || mc.gameSettings.keyBindForward.isKeyDown())) {
            mc.player.setSprinting(true);
        }
    }

    private boolean shouldSprint() {
        WTapModule wTapModule = ModuleRegistry.fromInstance(WTapModule.class);
        if (wTapModule != null && wTapModule.isToggled() && wTapModule.isTapping()) {
            return false;
        }
        return PlayerHelper.isMoving() && (mc.player.isCreative() || mc.player.getFoodStats().getFoodLevel() > 6);
    }
}
