package dev.rabies.vox.cheats;

import lombok.Getter;

import java.awt.*;

public enum Category {

    LEGIT("Legit", new Color(100, 255, 20)),
    RAGE("Rage", new Color(255, 60, 30)),
    OTHER("Other", new Color(60, 60, 60));

    @Getter String label;
    @Getter Color theme;

    Category(String label, Color theme) {
        this.label = label;
        this.theme = theme;
    }
}
