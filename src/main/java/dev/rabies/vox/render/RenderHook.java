package dev.rabies.vox.render;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.cheats.HudCheat;
import dev.rabies.vox.cheats.cheats.TabGuiCheat;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.render.hud.HudElement;
import dev.rabies.vox.render.hud.elements.ArrayListHud;
import dev.rabies.vox.render.hud.elements.WatermarkHud;
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

    @Getter private final HudCheat hudCheat;
    @Getter private final TabGuiCheat tabGuiCheat;

    public RenderHook() {
        hudCheat = (HudCheat) VoxMod.get().getCheatByName("hud");
        tabGuiCheat = (TabGuiCheat) VoxMod.get().getCheatByName("tabgui");
        Collections.addAll(elements,
                new ArrayListHud(),
                new WatermarkHud()
        );
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        if (mc.gameSettings.showDebugInfo) return;
        if (hudCheat != null && hudCheat.isEnabled()) {
            elements.stream().filter(HudElement::isVisible)
                    .forEach(hudElement -> hudElement.render(this, event));
        }

        if (tabGuiCheat != null && tabGuiCheat.isEnabled()) {
            tabGuiRenderer.render(this);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (mc.world == null || mc.gameSettings.showDebugInfo) return;
        if (!Keyboard.isCreated()) return;
        boolean state = Keyboard.getEventKeyState();
        int key = Keyboard.getEventKey();
        if (tabGuiCheat != null && tabGuiCheat.isEnabled()) {
            tabGuiRenderer.input(key, state);
        }
    }
}
