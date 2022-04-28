package dev.rabies.vox.render.tab;

import dev.rabies.vox.cheats.Cheat;
import lombok.Getter;
import org.lwjgl.input.Keyboard;

public class CheatTab implements TabActionListener {

    @Getter
    private final Cheat cheat;
    @Getter
    private final String label;

    public CheatTab(Cheat cheat) {
        this.cheat = cheat;
        this.label = cheat.getName();
    }

    @Override
    public void onInputKey(int keyCode, boolean state) {
        if (!state) return;

        switch (keyCode) {
            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_RETURN:
                cheat.toggle();
                break;

                // TODO: setting
        }
    }
}
