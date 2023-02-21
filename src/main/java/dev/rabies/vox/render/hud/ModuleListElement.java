package dev.rabies.vox.render.hud;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.events.render.RenderOverlayEvent;
import dev.rabies.vox.modules.ModuleRegistry;
import dev.rabies.vox.render.RenderHook;
import dev.rabies.vox.render.hud.HudElement;
import dev.rabies.vox.utils.render.Coloring;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleListElement extends HudElement {
    private final FontRenderer font = Minecraft.getMinecraft().fontRenderer;

    public ModuleListElement() {
        super("ModuleList");
    }

    @Override
    public void render(RenderHook hook, RenderOverlayEvent event) {
        List<Module> sortedModules = ModuleRegistry.getModules().stream().filter(Module::isToggled).sorted(Comparator.comparingDouble(it -> {
            String label = it.getName();
            if (it.getSuffix() != null && VoxMod.isDebugMode()) {
                label += " \2477" + it.getSuffix().toString();
            }
            return -font.getStringWidth(label);
        })).collect(Collectors.toList());

        ScaledResolution resolution = event.getResolution();
        double offsetY = resolution.getScaledHeight() - 15;
        for (Module module : sortedModules) {
            String label = module.getName();
            if (module.getSuffix() != null && VoxMod.isDebugMode()) {
                label += " \2477" + module.getSuffix().toString();
            }

            font.drawStringWithShadow(label, (float) resolution.getScaledWidth() - font.getStringWidth(label) - 5, (float) offsetY, Coloring.getRainbowColor(0.5, offsetY, 360).getRGB());
            offsetY -= font.FONT_HEIGHT + 1;
        }
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
