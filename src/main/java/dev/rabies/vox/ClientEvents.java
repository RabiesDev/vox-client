package dev.rabies.vox;

import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.setting.KeyBind;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class ClientEvents {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        assert Minecraft.getMinecraft().world != null;
        assert Keyboard.isCreated();

        boolean state = Keyboard.getEventKeyState();
        int key = Keyboard.getEventKey();
        if (key == 0) return;
        for (Cheat cheat : VoxMod.get().getCheats()) {
            if (cheat.getBind().getType() == KeyBind.BindType.TOGGLE && !state) continue;
            if (key != cheat.getBind().getKeyCode()) continue;
            cheat.toggle();
        }
    }
}
