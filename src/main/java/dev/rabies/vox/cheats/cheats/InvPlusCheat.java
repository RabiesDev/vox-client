package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.setting.BoolSetting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class InvPlusCheat extends Cheat {

    private final BoolSetting sneakSetting = registerBoolSetting("Sneak", false);
    private final KeyBinding[] moveKeys = {
            mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSneak
    };

    public InvPlusCheat() {
        super("Inventory+", Category.LEGIT);
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (mc.currentScreen == null) return;
        if (mc.currentScreen instanceof GuiChat) return;
        if (mc.currentScreen instanceof GuiEditSign) return;
        if (mc.currentScreen instanceof GuiRepair) return;

        for (int i = 0; i < (sneakSetting.getValue() ? moveKeys.length : moveKeys.length - 1); i++) {
            KeyBinding bind = moveKeys[i];
            KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
        }
    }
}
