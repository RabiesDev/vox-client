package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.NumberSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AimAssistCheat extends CheatWrapper {

    private final NumberSetting rangeSetting = registerNumberSetting("Range", 3.8, 3.0, 10.0, 0.1);
    private final BoolSetting doNotFunnySetting = registerBoolSetting("BruhMomento", false);

    public AimAssistCheat() {
        super("AimAssist", Category.LEGIT);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        // TODO: 1337
    }
}
