package dev.rabies.vox.settings;

import lombok.Getter;
import lombok.Setter;

public class KeyBinding {
    @Getter @Setter
    private int keyCode;
    @Getter @Setter
    private BindType type;

    public static KeyBinding none() {
        return new KeyBinding(-1, BindType.TOGGLE);
    }

    public static KeyBinding fromKey(int keyCode) {
        return new KeyBinding(keyCode, BindType.TOGGLE);
    }

    public KeyBinding(int keyCode, BindType type) {
        this.keyCode = keyCode;
        this.type = type;
    }

    public enum BindType {
        TOGGLE, HOLD
    }
}
