package dev.rabies.vox.cheats;

import dev.rabies.vox.cheats.setting.KeyBind;
import org.lwjgl.input.Keyboard;

public class DebugCheat extends Cheat {

    public DebugCheat() {
        super("Debug", new KeyBind(Keyboard.KEY_B, KeyBind.BindType.HOLD));
    }
}
