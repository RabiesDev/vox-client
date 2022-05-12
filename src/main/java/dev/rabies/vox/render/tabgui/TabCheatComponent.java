package dev.rabies.vox.render.tabgui;

import dev.rabies.vox.cheats.CheatWrapper;
import lombok.Getter;
import org.lwjgl.input.Keyboard;

public class TabCheatComponent implements TabActionListener {

    @Getter private final CheatWrapper cheat;
    @Getter private final String label;

    public TabCheatComponent(CheatWrapper cheat) {
        this.cheat = cheat;
        this.label = cheat.getName();
    }

    @Override
    public void input(int keyCode, boolean state) {
        if (!state) return;

        switch (keyCode) {
            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_RETURN:
                cheat.toggle();
                break;
        }
    }
}
