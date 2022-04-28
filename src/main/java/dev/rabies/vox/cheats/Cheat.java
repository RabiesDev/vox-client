package dev.rabies.vox.cheats;

import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.KeyBind;
import dev.rabies.vox.cheats.setting.Setting;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;

import java.util.LinkedList;
import java.util.function.Supplier;

public class Cheat implements ICheat {

    @Getter private final String name;
    @Getter private final KeyBind bind;
    @Getter private final Category category;
    @Getter private boolean enabled;

    @Getter
    private final LinkedList<Setting<?>> settings;

    public Cheat(String name, Category category) {
        this(name, category, KeyBind.none());
    }

    public Cheat(String name, Category category, KeyBind bind) {
        this.name = name;
        this.category = category;
        this.bind = bind;
        this.settings = new LinkedList<>();
    }

    protected BoolSetting registerBoolSetting(String name, Boolean state) {
        return registerBoolSetting(name, state, () -> true);
    }

    protected BoolSetting registerBoolSetting(String name, Boolean state, Supplier<Boolean> dependency) {
        BoolSetting setting = new BoolSetting(name, state, dependency);
        settings.add(setting);
        return setting;
    }

    @Override
    public void toggle() {
        enabled = !enabled;

        // TODO: event
        if (enabled) {
            onEnable();
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            onDisable();
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
