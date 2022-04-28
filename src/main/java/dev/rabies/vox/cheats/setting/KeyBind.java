package dev.rabies.vox.cheats.setting;

import lombok.Getter;
import lombok.Setter;

public class KeyBind {

    @Getter @Setter
    private int keyCode;
    @Getter @Setter
    private BindType type;

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

    public void update(int keyCode, String type) {
        this.keyCode = keyCode;
        this.type = BindType.getByString(type);
    }

    public enum BindType {
        TOGGLE,
        HOLD;

        static BindType getByString(String name) {
            switch (name.toLowerCase().trim()) {
                case "toggle":
                case "t":
                    return TOGGLE;
                case "hold":
                case "h":
                    return HOLD;
            }
            return TOGGLE;
        }
    }
}
