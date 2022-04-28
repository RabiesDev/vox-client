package dev.rabies.vox.render;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.Render2DEvent;
import dev.rabies.vox.render.tab.TabGuiWidget;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collections;

public class RenderHook {

    @Getter
    private static ArrayList<Widget> widgets;
    private Cheat hudCheat;

    public RenderHook() {
        widgets = new ArrayList<>();
        Collections.addAll(widgets,
                new WatermarkWidget(),
                new CheatInfoWidget(),
                new TabGuiWidget()
        );
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        if (hudCheat == null) {
            hudCheat = VoxMod.get().getCheatByName("Hud");
            return;
        }

        if (!hudCheat.isEnabled()) return;
        widgets.stream().filter(Widget::isVisible)
                .forEach(it -> it.draw(event));
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().world == null) return;
        if (!Keyboard.isCreated()) return;

        if (hudCheat == null) return;
        if (!hudCheat.isEnabled()) return;
        boolean state = Keyboard.getEventKeyState();
        int key = Keyboard.getEventKey();
        widgets.forEach(it -> it.onInputKey(key ,state));
    }

    public static Widget getWidgetByName(String name) {
        return widgets.stream().filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
