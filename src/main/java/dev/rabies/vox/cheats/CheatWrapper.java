package dev.rabies.vox.cheats;

import dev.rabies.vox.cheats.setting.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

import java.util.LinkedList;
import java.util.function.Supplier;

public class CheatWrapper implements Cheat {

    @Getter private final String name;
    @Getter private final KeyBind bind;
    @Getter private final Category category;
    @Getter private boolean enabled;
    @Getter @Setter
    private Object suffix;

    @Getter
    private final LinkedList<Setting<?>> settings;

    public CheatWrapper(String name, Category category) {
        this(name, category, KeyBind.none());
    }

    public CheatWrapper(String name, Category category, KeyBind bind) {
        this.name = name;
        this.category = category;
        this.bind = bind;
        this.settings = new LinkedList<>();
    }

    public Setting<?> getSettingByName(String name) {
        return settings.stream().filter(it -> it.getLabel().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    protected ModeSetting registerModeSetting(String name, Enum<?> defaultValue) {
        return registerModeSetting(name, defaultValue, () -> true);
    }

    protected ModeSetting registerModeSetting(String name, Enum<?> defaultValue, Supplier<Boolean> dependency) {
        ModeSetting modeSetting = new ModeSetting(name, defaultValue, dependency);
        settings.add(modeSetting);
        return modeSetting;
    }

    protected BoolSetting registerBoolSetting(String name, Boolean state) {
        return registerBoolSetting(name, state, () -> true);
    }

    protected BoolSetting registerBoolSetting(String name, Boolean state, Supplier<Boolean> dependency) {
        BoolSetting setting = new BoolSetting(name, state, dependency);
        settings.add(setting);
        return setting;
    }

    protected NumberSetting registerNumberSetting(String name, double value,
                                                  double minValue, double maxValue, double increment) {
        return registerNumberSetting(name, value, minValue, maxValue, increment, () -> true);
    }

    protected NumberSetting registerNumberSetting(String name, double value,
                                                  double minValue, double maxValue, double increment,
                                                  Supplier<Boolean> dependency) {
        NumberSetting setting = new NumberSetting(name, value, minValue, maxValue, increment, dependency);
        settings.add(setting);
        return setting;
    }

    protected void sendPacket(Packet<?> packetIn) {
        mc.player.connection.sendPacket(packetIn);
    }

    @Override
    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            onEnable();
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            onDisable();
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
