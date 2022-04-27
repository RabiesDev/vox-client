package dev.rabies.vox.cheats;

import dev.rabies.vox.cheats.setting.KeyBind;
import org.lwjgl.input.Keyboard;

public class AutoClicker extends Cheat {

    public AutoClicker() {
        super("AutoClicker", KeyBind.fromKey(Keyboard.KEY_M));
    }
}
