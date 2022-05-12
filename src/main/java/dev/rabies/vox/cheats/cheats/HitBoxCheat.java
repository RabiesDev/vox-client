package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.NumberSetting;

public class HitBoxCheat extends CheatWrapper {

    public final NumberSetting sizeSetting = registerNumberSetting("Size", 0.6f, 0.0f, 10.0f, 0.1f);

    public HitBoxCheat() {
        super("HitBox", Category.LEGIT);
    }
}
