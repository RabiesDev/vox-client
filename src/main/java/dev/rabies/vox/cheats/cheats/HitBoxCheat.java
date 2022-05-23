package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.NumberSetting;

public class HitBoxCheat extends CheatWrapper {

    public final NumberSetting sizeSetting = registerNumberSetting("Size", 0.08f, 0.0f, 3.0f, 0.01f);

    public HitBoxCheat() {
        super("HitBox", Category.LEGIT);
    }
}
