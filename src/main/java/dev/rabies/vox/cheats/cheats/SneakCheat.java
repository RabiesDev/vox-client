package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.ModeSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SneakCheat extends CheatWrapper {

    enum Mode {
        Normal, Always, Bypass
    }

    private final ModeSetting<Mode> modeSetting = registerModeSetting("Mode", Mode.Bypass);

    public SneakCheat() {
        super("Sneak", Category.RAGE);
    }

    @Override
    public void onDisable() {
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
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
}
