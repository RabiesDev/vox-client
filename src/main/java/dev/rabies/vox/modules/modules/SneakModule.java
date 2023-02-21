package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.ModeSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SneakModule extends Module {
    private final ModeSetting<Mode> modeSetting = registerSetting(ModeSetting.<Mode>builder()
            .name("Mode")
            .value(Mode.Bypass)
            .build()
    );

    public SneakModule() {
        super("Sneak", Category.RAGE);
    }

    @Override
    public void onDisable() {
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        if (mc.player.isSneaking()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        switch (modeSetting.getValue()) {
            case Normal:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                break;

            case Always:
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                break;

            case Bypass:
                if (event.isPre()) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                } else {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
                break;
        }
    }

    public enum Mode {
        Normal, Always, Bypass
    }
}
