package dev.rabies.vox.cheats.setting;

import java.util.function.Supplier;

public class BoolSetting extends Setting<Boolean> {

    public BoolSetting(String label, Boolean value, Supplier<Boolean> dependency) {
        super(label, value, dependency);
    }
}
