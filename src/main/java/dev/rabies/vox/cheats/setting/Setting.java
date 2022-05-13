package dev.rabies.vox.cheats.setting;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

abstract public class Setting<V> {

    @Getter protected final String label;
    protected final Supplier<Boolean> dependency;
    @Getter @Setter
    protected V value;

    public Setting(final String label, V value, Supplier<Boolean> dependency) {
        this.label = label;
        this.dependency = dependency;
        this.value = value;
    }

    public boolean isAvailable() {
        return dependency.get();
    }

    abstract public void load(JsonElement obj);

    abstract public JsonElement save();
}
