package dev.rabies.vox.render;

import dev.rabies.vox.modules.ModuleRegistry;
import dev.rabies.vox.modules.modules.HudModule;
import dev.rabies.vox.modules.modules.TabGuiModule;
import dev.rabies.vox.events.render.RenderOverlayEvent;
import dev.rabies.vox.render.hud.HudElement;
import dev.rabies.vox.render.hud.ModuleListElement;
import dev.rabies.vox.render.hud.WatermarkElement;
import dev.rabies.vox.render.tabgui.TabGuiRenderer;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderHook {
    public final Minecraft mc = Minecraft.getMinecraft();
    private final List<HudElement> elements = new ArrayList<>();
    private final TabGuiRenderer tabGuiRenderer = new TabGuiRenderer();

    @Getter
    private final HudModule hudModule = ModuleRegistry.fromInstance(HudModule.class);
    @Getter
    private final TabGuiModule tabGuiModule = ModuleRegistry.fromInstance(TabGuiModule.class);

    public RenderHook() {
        Collections.addAll(elements,
                new ModuleListElement(),
                new WatermarkElement()
        );
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderOverlayEvent event) {
        if (mc.gameSettings.showDebugInfo) return;
        if (hudModule != null && hudModule.isToggled()) {
            elements.stream().filter(HudElement::isVisible).forEach(hudElement ->
                    hudElement.render(this, event)
            );
        }

        if (tabGuiModule != null && tabGuiModule.isToggled()) {
            tabGuiRenderer.render(this);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (mc.gameSettings.showDebugInfo) return;
        boolean active = Keyboard.getEventKeyState();
        int keyCode = Keyboard.getEventKey();
        if (tabGuiModule != null && tabGuiModule.isToggled()) {
            tabGuiRenderer.input(keyCode, active);
        }
    }
}
