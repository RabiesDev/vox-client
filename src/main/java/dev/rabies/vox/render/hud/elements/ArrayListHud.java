package dev.rabies.vox.render.hud.elements;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.render.RenderHook;
import dev.rabies.vox.render.font.SystemFontRenderer;
import dev.rabies.vox.render.hud.HudElement;
import dev.rabies.vox.utils.render.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListHud extends HudElement {

    private final SystemFontRenderer cheatInfoFont = VoxMod.get().newSystemFont("NotoSansJP-Medium", 19);
    private final FontRenderer defaultCheatInfoFont = Minecraft.getMinecraft().fontRenderer;

    public ArrayListHud() {
        super("ArrayList");
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void render(RenderHook hook, Render2DEvent event) {
        List<CheatWrapper> sorted = VoxMod.get().getCheats().stream()
                .filter(CheatWrapper::isEnabled)
                .sorted(Comparator.comparingDouble(it -> {
                    String label = it.getName();
                    if (it.getSuffix() != null && it.getSuffix().toString().length() > 0 &&
                            VoxMod.get().isDebugMode()) {
                        label += " \2477" + it.getSuffix().toString();
                    }
                    return -defaultCheatInfoFont.getStringWidth(label);
                })).collect(Collectors.toList());

        ScaledResolution sr = event.getResolution();
        double offsetY = sr.getScaledHeight() - 15;
        for (CheatWrapper cheat : sorted) {
            String label = cheat.getName();
            if (cheat.getSuffix() != null && cheat.getSuffix().toString().length() > 0 &&
                    VoxMod.get().isDebugMode()) {
                label += " \2477" + cheat.getSuffix().toString();
            }

            int r = ColorUtil.getRainbowColor(0.5, offsetY, 360).getRGB();
            double x = sr.getScaledWidth() - defaultCheatInfoFont.getStringWidth(label);
            defaultCheatInfoFont.drawStringWithShadow(label, (float) x - 5, (float) offsetY, r);
            offsetY -= defaultCheatInfoFont.FONT_HEIGHT + 1;
        }
    }
}
