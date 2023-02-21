package dev.rabies.vox.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

public class NumberSetting extends BaseSetting<Double> {
    @Getter
    private final double min;
    @Getter
    private final double max;
    @Getter
    private final double interval;

    @Builder
    public NumberSetting(String name, double value, double min, double max, double interval, Supplier<Boolean> dependency) {
        super(name, value, dependency);
        this.min = min;
        this.max = max;
        this.interval = interval;
    }

    @Override
    public void setValue(Double value) {
        super.setValue(MathHelper.clamp(value, min, max));
    }

    @Override
    public JsonElement serialize() {
        return new JsonPrimitive(value);
    }

    @Override
    public void deserialize(JsonElement element) {
        value = element.getAsDouble();
    }
}
