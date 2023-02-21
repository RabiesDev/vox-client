package dev.rabies.vox.settings;

import dev.rabies.vox.config.Serializable;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

public abstract class BaseSetting<V> implements Serializable {
    @Getter
    protected final String name;
    @Getter @Setter
    protected V value;

    private final Supplier<Boolean> dependency;

    public BaseSetting(final String name, V value, Supplier<Boolean> dependency) {
        this.name = name;
        this.value = value;
        this.dependency = dependency;
    }

    public boolean isAvailable() {
        return dependency.get();
    }
}
