package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.NumberSetting;

public class HitBoxModule extends Module {
    public final NumberSetting sizeSetting = registerSetting(NumberSetting.builder()
            .name("Size")
            .value(0.08)
            .min(0.00)
            .max(3.0)
            .interval(0.01)
            .build()
    );

    public HitBoxModule() {
        super("HitBox", Category.LEGIT);
    }
}
