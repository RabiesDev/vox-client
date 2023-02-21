package dev.rabies.vox.modules;

import lombok.Getter;

import java.awt.*;

public enum Category {
    LEGIT("Legit", new Color(100, 255, 20)),
    RAGE("Rage", new Color(255, 60, 30)),
    OTHER("Other", new Color(60, 60, 60));

    @Getter
    final String label;
    @Getter
    final Color color;

    Category(String label, Color color) {
        this.label = label;
        this.color = color;
    }
}
