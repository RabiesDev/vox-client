package dev.rabies.vox.cheats.setting;

import lombok.Getter;
import lombok.Setter;

public class KeyBind {

    @Getter @Setter private int keyCode;
    @Getter @Setter private BindType type;

    public static KeyBind none() {
        return new KeyBind(-1, BindType.TOGGLE);
    }

    public static KeyBind fromKey(int keyCode) {
        return new KeyBind(keyCode, BindType.TOGGLE);
    }

    public KeyBind(int keyCode, BindType type) {
        this.keyCode = keyCode;
        this.type = type;
    }

    public enum BindType {
        TOGGLE,
        HOLD
    }
}
