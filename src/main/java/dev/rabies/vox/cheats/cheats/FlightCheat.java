package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.NumberSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.PlayerUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FlightCheat extends CheatWrapper {

    private final NumberSetting speedSetting = registerNumberSetting("Speed", 1.0, 0.1, 5.0, 0.1);

    public FlightCheat() {
        super("Flight", Category.RAGE);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        double speed = speedSetting.getValue();
        if (mc.gameSettings.keyBindSprint.isKeyDown())
            speed *= 1.5;

        mc.player.setVelocity(0, 0, 0);
        if (mc.gameSettings.keyBindJump.isKeyDown())
            mc.player.motionY = speed / 1.8;
        else if (mc.gameSettings.keyBindSneak.isKeyDown())
            mc.player.motionY = -speed / 1.8;
        PlayerUtils.setMotion(speed);
    }
}
