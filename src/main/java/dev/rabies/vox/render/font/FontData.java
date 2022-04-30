package dev.rabies.vox.render.font;

import lombok.Data;

@Data
public class FontData {

    private final String name;
    private final int size;
    private final SystemFontRenderer renderer;

}
