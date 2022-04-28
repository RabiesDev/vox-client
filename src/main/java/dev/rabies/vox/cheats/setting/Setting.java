package dev.rabies.vox.cheats.setting;

import lombok.Getter;

import java.util.function.Supplier;

public class Setting<V> {

    @Getter protected final String label;
    protected final Supplier<Boolean> dependency;
    @Getter protected V value;

    public Setting(final String label, V value) {
        this(label, value, () -> true);
    }

    public Setting(final String label, V value, Supplier<Boolean> dependency) {
        this.label = label;
        this.dependency = dependency;
        this.value = value;
    }

    public boolean isAvailable() {
        return dependency.get();
    }
}
