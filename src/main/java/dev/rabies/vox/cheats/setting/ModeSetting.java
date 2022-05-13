package dev.rabies.vox.cheats.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Getter;

import java.util.function.Supplier;

public class ModeSetting<V extends Enum<V>> extends Setting<V> {

    @Getter
    private final V[] values;

    public ModeSetting(String label, V value, Supplier<Boolean> dependency) {
        super(label, value, dependency);
        this.values = (V[]) value.getClass().getEnumConstants();
    }

    public boolean is(Enum<?> v) {
        return value == v;
    }

    public void setEnumValue(String name) {
        for (V possibleValue : values) {
            if (possibleValue.name().equalsIgnoreCase(name)) {
                setValue(possibleValue);
                break;
            }
        }
    }

    @Override
    public void load(JsonElement obj) {
        setEnumValue(obj.getAsString());
    }

    @Override
    public JsonElement save() {
        return new JsonPrimitive(value.name());
    }
}
