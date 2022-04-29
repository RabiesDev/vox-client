package dev.rabies.vox.cheats.setting;

import lombok.Getter;

import java.util.function.Supplier;

public class NumberSetting extends Setting<Double> {

    @Getter
    private final double maxValue;
    @Getter
    private final double minValue;
    @Getter
    private final double increment;

    public NumberSetting(String label, double value,
                         double minValue, double maxValue, double increment,
                         Supplier<Boolean> dependency) {
        super(label, value, dependency);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
    }

    @Override
    public void setValue(Double value) {
        if (value == null) return;
        if (value < minValue) {
            value = minValue;
        } else if (value > maxValue) {
            value = maxValue;
        }
        super.setValue(value);
    }
}
