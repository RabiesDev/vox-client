package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.BooleanSetting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class InventoryPlusModule extends Module {
    private final BooleanSetting allowSneakSetting = registerSetting(BooleanSetting.builder()
            .name("Allow Sneak")
            .value(true)
            .build()
    );

    private final KeyBinding[] moveKeys = {
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindJump,
            mc.gameSettings.keyBindSneak
    };

    public InventoryPlusModule() {
        super("Inventory+", Category.LEGIT);
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (mc.currentScreen == null) return;
        if (mc.currentScreen instanceof GuiChat) return;
        if (mc.currentScreen instanceof GuiEditSign) return;
        if (mc.currentScreen instanceof GuiRepair) return;

        for (int i = 0; i < (allowSneakSetting.getValue() ? moveKeys.length : moveKeys.length - 1); i++) {
            KeyBinding bind = moveKeys[i];
            KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
        }
    }
}
