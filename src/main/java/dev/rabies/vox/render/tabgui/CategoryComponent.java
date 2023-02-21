package dev.rabies.vox.render.tabgui;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.utils.render.DrawHelper;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryComponent {
    private final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
    @Getter
    private final List<ModuleComponent> cheatTabs = new ArrayList<>();
    @Getter
    private final Category category;
    @Getter
    private final String label;

    public CategoryComponent(Category category) {
        this.category = category;
        this.label = category.getLabel();
    }

    public void registerTab(ModuleComponent tab) {
        cheatTabs.add(tab);
    }

    public void renderCheatsTab(double parentOffsetX, double parentOffsetY, int selectedIndex) {
        int offset = 6;
        double height = ((font.FONT_HEIGHT / 1.2) + offset - 1) * cheatTabs.size() + 2;
        double width = 0;
        for (ModuleComponent tab : cheatTabs) {
            if (font.getStringWidth(tab.getLabel()) > width) {
                width = font.getStringWidth(tab.getLabel());
            }
        }
        width += offset * 3;

        int backgroundColor = new Color(20, 20, 20, 200).getRGB();
        int themeColor = new Color(110, 255, 60).getRGB();

        GlStateManager.translate(parentOffsetX, parentOffsetY, 0);
        DrawHelper.drawRect(0, 0, width, height, backgroundColor);
        GlStateManager.glLineWidth(1.2F);
        DrawHelper.drawRect(GL11.GL_LINE_LOOP, 0, 0, width, height, themeColor);

        int subOffset = offset - 1;
        int offsetY = subOffset - 1;
        for (int i = 0; i < cheatTabs.size(); i++) {
            ModuleComponent tab = cheatTabs.get(i);
            Color color = new Color(255, 255, 255).darker();
            if (tab.getModule().isToggled()) {
                color = new Color(110, 255, 60).darker();
            }

            if (selectedIndex == i) {
                color = color.brighter();
            }

            font.drawStringWithShadow(tab.getLabel(), subOffset, offsetY, color.getRGB());
            offsetY += font.FONT_HEIGHT / 1.2;
            offsetY += subOffset;
        }

        GlStateManager.translate(-parentOffsetX, -parentOffsetY, 0);
    }
}
