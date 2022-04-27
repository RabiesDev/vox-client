package dev.rabies.vox.cheats;

import dev.rabies.vox.cheats.setting.KeyBind;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;

public class Cheat implements ICheat {

    @Getter private final String name;
    @Getter private final KeyBind bind;
    @Getter private boolean enabled;

    public Cheat(String name) {
        this.name = name;
        this.bind = KeyBind.none();
    }

    public Cheat(String name, KeyBind bind) {
        this.name = name;
        this.bind = bind;
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
