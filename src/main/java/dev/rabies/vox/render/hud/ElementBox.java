package dev.rabies.vox.render.hud;

import lombok.Getter;
import lombok.Setter;

public class ElementBox {

    @Getter @Setter
    private double posX, posY;
    @Getter @Setter
    private double width, height;

    public void setPos(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
}
