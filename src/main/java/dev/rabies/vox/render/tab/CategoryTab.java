package dev.rabies.vox.render.tab;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.utils.DrawUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryTab {

    @Getter
    private final List<CheatTab> cheatTabs = new ArrayList<>();
    @Getter
    private final Category category;
    @Getter
    private final String label;

    public CategoryTab(Category category) {
        this.category = category;
        this.label = category.getLabel();
    }

    public void registerTab(CheatTab tab) {
        cheatTabs.add(tab);
    }

    public void renderCheatsTab(int parentOffsetX, int parentOffsetY, int selectedIndex) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;

        int offset = 5;
        int height = (font.FONT_HEIGHT + offset) * cheatTabs.size();
        int width = 0;
        for (CheatTab tab : cheatTabs) {
            if (font.getStringWidth(tab.getLabel()) > width) {
                width = font.getStringWidth(tab.getLabel());
            }
        }
        width += offset * 1.5;

        int bg = new Color(20, 20, 20, 200).getRGB();
        int theme = new Color(110, 255, 60).getRGB();
        int theme_bg = new Color(110, 255, 60, 200).getRGB();

        GlStateManager.translate(parentOffsetX, parentOffsetY, 0);
        DrawUtils.drawRect(0, 0, width, height, bg);
        GlStateManager.glLineWidth(1.2F);
        DrawUtils.drawRect(GL11.GL_LINE_LOOP, 0, 0, width, height, theme);

        int subOffset = offset - 1;
        int offsetY = subOffset - 1;
        for (int i = 0; i < cheatTabs.size(); i++) {
            CheatTab tab = cheatTabs.get(i);
            Color col = new Color(255, 255, 255).darker();
            if (tab.getCheat().isEnabled()) {
            	col = new Color(110, 255, 60).darker();
            }
            
            if (selectedIndex == i) {
                col = col.brighter();
            }

            font.drawStringWithShadow(tab.getLabel(), subOffset, offsetY + 1, col.getRGB());
            offsetY += font.FONT_HEIGHT;
            offsetY += subOffset;
        }

        GlStateManager.translate(-parentOffsetX, -parentOffsetY, 0);
    }
}
