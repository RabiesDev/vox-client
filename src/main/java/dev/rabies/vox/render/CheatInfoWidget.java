package dev.rabies.vox.render;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.Render2DEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CheatInfoWidget extends Widget {

    public CheatInfoWidget() {
        super("CheatInfo");
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void draw(Render2DEvent event) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        List<Cheat> sorted = VoxMod.get().getCheats().stream()
                .filter(Cheat::isEnabled)
                .sorted(Comparator.comparingDouble(module -> -font.getStringWidth(module.getName())))
                .collect(Collectors.toList());

        int offsetY = RenderHook.getWidgetByName("tabgui").isVisible() ? 65 : font.FONT_HEIGHT + 2;
        for (Cheat cheat : sorted) {
            font.drawStringWithShadow(cheat.getName(), 4, offsetY, -1);
            offsetY += font.FONT_HEIGHT;
        }
    }

    @Override
    public void onInputKey(int keyCode, boolean state) {
    }
}
