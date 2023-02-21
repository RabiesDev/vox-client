package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.NumberSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.PlayerHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FlightModule extends Module {
    private final NumberSetting boostSetting = registerSetting(NumberSetting.builder()
            .name("Boost")
            .value(1.0)
            .min(0.1)
            .max(4.0)
            .interval(0.1)
            .build()
    );

    public FlightModule() {
        super("Flight", Category.RAGE);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        double boost = boostSetting.getValue();
        if (mc.gameSettings.keyBindSprint.isKeyDown()) {
            boost *= 1.5;
        }

        mc.player.setVelocity(0, 0, 0);
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY = boost / 1.8;
        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY = -boost / 1.8;
        }
        PlayerHelper.setMotion(boost);
    }
}
