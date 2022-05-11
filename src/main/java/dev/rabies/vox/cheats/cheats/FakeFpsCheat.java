package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import lombok.Getter;

// wtf
public class FakeFpsCheat extends CheatWrapper {

    @Getter
    private static FakeFpsCheat instance;

    public FakeFpsCheat() {
        super("Fake Fps", Category.OTHER);
        instance = this;
    }
}
