package dev.rabies.vox.render.tabgui;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.ModuleRegistry;
import dev.rabies.vox.render.RenderHook;
import dev.rabies.vox.utils.render.DrawHelper;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabGuiRenderer implements InputListener {
    private final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
    @Getter
    private final List<CategoryComponent> categoryTabs = new ArrayList<>();
    @Getter
    private int selectedIndex;
    @Getter
    private int selectedCheatIndex;

    public TabGuiRenderer() {
        Arrays.stream(Category.values()).forEach(category -> {
            CategoryComponent tabComponent = new CategoryComponent(category);
            ModuleRegistry.getModules(category).forEach(module ->
                    tabComponent.registerTab(new ModuleComponent(module))
            );
            categoryTabs.add(tabComponent);
        });
        selectedIndex = 0;
        selectedCheatIndex = -1;
    }

    public void render(RenderHook hook) {
        int offset = 6;
        double height = ((font.FONT_HEIGHT / 1.2) + offset - 1) * categoryTabs.size() + 2;
        double width = 0;
        for (CategoryComponent tabComponent : categoryTabs) {
            if (font.getStringWidth(tabComponent.getLabel()) > width) {
                width = font.getStringWidth(tabComponent.getLabel());
            }
        }
        width += offset * 3;

        int backgroundColor = new Color(20, 20, 20, 200).getRGB();
        int themeColor = new Color(110, 255, 60).getRGB();

        GlStateManager.translate(5, hook.getHudModule().isToggled() ? 20 : 5, 0);
        DrawHelper.drawRect(0, 0, width, height, backgroundColor);
        GlStateManager.glLineWidth(1.2F);
        DrawHelper.drawRect(GL11.GL_LINE_LOOP, 0, 0, width, height, themeColor);

        int subOffset = offset - 1;
        int offsetY = subOffset - 1;
        for (int i = 0; i < categoryTabs.size(); i++) {
            CategoryComponent tabComponent = categoryTabs.get(i);
            Color color = new Color(255, 255, 255).darker();
            if (selectedIndex == i) {
                color = color.brighter().brighter();

                if (selectedCheatIndex != -1) {
                    color = new Color(110, 255, 60);
                    tabComponent.renderCheatsTab(width + 3, offsetY - 4, selectedCheatIndex);
                }
            }

            font.drawStringWithShadow(tabComponent.getLabel(), subOffset, offsetY, color.getRGB());
            offsetY += font.FONT_HEIGHT / 1.2;
            offsetY += subOffset;
        }

        GlStateManager.translate(-5, hook.getHudModule().isToggled() ? -20 : -5, 0);
    }

    @Override
    public void input(int keyCode, boolean state) {
        if (state) {
            switch (keyCode) {
                case Keyboard.KEY_DOWN:
                    if (selectedCheatIndex != -1) {
                        selectedCheatIndex++;
                        if (selectedCheatIndex >= categoryTabs.get(selectedIndex).getCheatTabs().size()) {
                            selectedCheatIndex = 0;
                        }
                        return;
                    }

                    selectedIndex++;
                    if (selectedIndex >= categoryTabs.size()) {
                        selectedIndex = 0;
                    }
                    break;

                case Keyboard.KEY_UP:
                    if (selectedCheatIndex != -1) {
                        selectedCheatIndex--;
                        if (selectedCheatIndex < 0) {
                            selectedCheatIndex = categoryTabs.get(selectedIndex).getCheatTabs().size() - 1;
                        }
                        return;
                    }

                    selectedIndex--;
                    if (selectedIndex < 0) {
                        selectedIndex = categoryTabs.size() - 1;
                    }
                    break;

                case Keyboard.KEY_RIGHT:
                case Keyboard.KEY_RETURN: // enter
                    CategoryComponent tab = categoryTabs.get(selectedIndex);
                    if (selectedCheatIndex != -1) {
                        tab.getCheatTabs().get(selectedCheatIndex).input(keyCode, true);
                        return;
                    }

                    if (!tab.getCheatTabs().isEmpty()) {
                        selectedCheatIndex = 0;
                    }
                    break;

                case Keyboard.KEY_LEFT:
                    if (selectedCheatIndex != -1) {
                        selectedCheatIndex = -1;
                    }
                    break;
            }
        }
    }
}
