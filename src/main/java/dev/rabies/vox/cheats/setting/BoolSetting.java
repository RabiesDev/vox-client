package dev.rabies.vox.cheats.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.function.Supplier;

public class BoolSetting extends Setting<Boolean> {

    public BoolSetting(String label, Boolean value, Supplier<Boolean> dependency) {
        super(label, value, dependency);
    }

    @Override
    public void load(JsonElement obj) {
        value = obj.getAsBoolean();
    }

    @Override
    public JsonElement save() {
        return new JsonPrimitive(value);
    }
}
