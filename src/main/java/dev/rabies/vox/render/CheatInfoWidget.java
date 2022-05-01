package dev.rabies.vox.render;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.Render2DEvent;
import dev.rabies.vox.render.font.SystemFontRenderer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CheatInfoWidget extends Widget {

    private final SystemFontRenderer cheatInfoFont = VoxMod.get().newSystemFont("Mukta-SemiBold", 20);

    public CheatInfoWidget() {
        super("CheatInfo");
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void draw(Render2DEvent event) {
        List<Cheat> sorted = VoxMod.get().getCheats().stream()
                .filter(Cheat::isEnabled)
                .sorted(Comparator.comparingDouble(it -> {
                    String label = it.getName();
                    if (it.getSuffix() != null && it.getSuffix().toString().length() > 0 &&
                            VoxMod.get().isDebugMode()) {
                        label += " \2477" + it.getSuffix().toString();
                    }
                    return -cheatInfoFont.getStringWidth(label);
                })).collect(Collectors.toList());

        double offsetY = RenderHook.getWidgetByName("tabgui").isVisible() ? 68 : cheatInfoFont.getHeight() + 2;
        for (Cheat cheat : sorted) {
            String label = cheat.getName();
            if (cheat.getSuffix() != null && cheat.getSuffix().toString().length() > 0 &&
                    VoxMod.get().isDebugMode()) {
                label += " \2477" + cheat.getSuffix().toString();
            }

            cheatInfoFont.drawStringWithShadow(label, 5, offsetY, -1);
            offsetY += cheatInfoFont.getHeight();
        }
    }

    @Override
    public void onInputKey(int keyCode, boolean state) {
    }
}
