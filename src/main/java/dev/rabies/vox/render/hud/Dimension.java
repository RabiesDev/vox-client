package dev.rabies.vox.render.hud;

import lombok.Getter;
import lombok.Setter;

public class Dimension {
    @Getter @Setter
    private double x, y;
    @Getter @Setter
    private double w, h;

    public void setPos(double posX, double posY) {
        this.x = posX;
        this.y = posY;
    }

    public void setSize(double width, double height) {
        this.w = width;
        this.h = height;
    }
}
