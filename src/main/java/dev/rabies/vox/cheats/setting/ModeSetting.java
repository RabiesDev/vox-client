package dev.rabies.vox.cheats.setting;

import lombok.Getter;

import java.util.function.Supplier;

public class ModeSetting<V extends Enum<V>> extends Setting<V> {

    @Getter
    private final V[] values;

    public ModeSetting(String label, V value) {
        super(label, value, () -> true);
        this.values = (V[]) value.getClass().getEnumConstants();
    }

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
}
