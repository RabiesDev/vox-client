package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.render.hud.HudElement;
import dev.rabies.vox.render.hud.elements.ArrayListHud;
import dev.rabies.vox.render.hud.elements.WatermarkHud;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HudCheat extends CheatWrapper {

    public HudCheat() {
        super("HUD", Category.OTHER);
    }
}
