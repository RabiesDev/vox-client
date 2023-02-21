package dev.rabies.vox.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Builder;
import lombok.Getter;

import java.util.function.Supplier;

public class ModeSetting<T extends Enum<T>> extends BaseSetting<T> {
    @Getter
    private final T[] values;

    @Builder
    public ModeSetting(String name, T value, Supplier<Boolean> dependency) {
        super(name, value, dependency);
        this.values = (T[]) value.getClass().getEnumConstants();
    }

    public boolean match(Enum<?> value) {
        return this.value.equals(value);
    }

    @Override
    public JsonElement serialize() {
        return new JsonPrimitive(value.name());
    }

    @Override
    public void deserialize(JsonElement element) {
        for (T possibleValue : values) {
            if (possibleValue.name().equals(element.getAsString())) {
                setValue(possibleValue);
                break;
            }
        }
    }
}
