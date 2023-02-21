package dev.rabies.vox.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Builder;

import java.util.function.Supplier;

public class BooleanSetting extends BaseSetting<Boolean> {
    @Builder
    public BooleanSetting(String name, Boolean value, Supplier<Boolean> dependency) {
        super(name, value, dependency);
    }

    @Override
    public JsonElement serialize() {
        return new JsonPrimitive(value);
    }

    @Override
    public void deserialize(JsonElement element) {
        value = element.getAsBoolean();
    }
}
