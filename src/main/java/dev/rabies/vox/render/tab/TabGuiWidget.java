package dev.rabies.vox.render.tab;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.events.Render2DEvent;
import dev.rabies.vox.render.Widget;
import dev.rabies.vox.utils.DrawUtils;
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

public class TabGuiWidget extends Widget implements TabActionListener {

    private final List<CategoryTab> categoryTabs = new ArrayList<>();
    @Getter
    private int selectedIndex;
    @Getter
    private int selectedCheatIndex;

    public TabGuiWidget() {
        super("TabGui");
        Arrays.stream(Category.values()).forEach(cat -> {
            CategoryTab tab = new CategoryTab(cat);
            VoxMod.get().getCheatsByCategory(cat).forEach(ch ->
                    tab.registerTab(new CheatTab(ch))
            );
            categoryTabs.add(tab);
        });
        selectedIndex = 0;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void draw(Render2DEvent event) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        
        int offset = 5;
        int height = (font.FONT_HEIGHT + offset) * categoryTabs.size();
        int width = 0;
        for (CategoryTab tab : categoryTabs) {
            if (font.getStringWidth(tab.getLabel()) > width) {
                width = font.getStringWidth(tab.getLabel());
            }
        }
        width += offset * 1.5;

        int bg = new Color(20, 20, 20, 200).getRGB();
        int theme = new Color(110, 255, 60).getRGB();
        int theme_bg = new Color(110, 255, 60, 200).getRGB();

        GlStateManager.translate(4, 15, 0);
        DrawUtils.drawRect(0, 0, width, height, bg);
        GlStateManager.glLineWidth(1.2F);
        DrawUtils.drawRect(GL11.GL_LINE_LOOP, 0, 0, width, height, theme);

        int subOffset = offset - 1;
        int offsetY = subOffset - 1;
        for (int i = 0; i < categoryTabs.size(); i++) {
            CategoryTab tab = categoryTabs.get(i);
            Color col = new Color(255, 255, 255).darker();
            if (selectedIndex == i) {
//                DrawUtils.drawRect(0, offsetY - subOffset + 1, width, offsetY + font.FONT_HEIGHT + offset - 1, theme_bg);
                col = col.brighter().brighter();

                if (selectedCheatIndex != -1) {
                	col = new Color(110, 255, 60);
                    tab.renderCheatsTab(width + 2, offsetY - 2, selectedCheatIndex);
                }
            }

            font.drawStringWithShadow(tab.getLabel(), subOffset, offsetY + 1, col.getRGB());
            offsetY += font.FONT_HEIGHT;
            offsetY += subOffset;
        }

        GlStateManager.translate(-4, -15, 0);
    }

    @Override
    public void onInputKey(int keyCode, boolean state) {
    	if (!state) return;

        switch (keyCode) {
            case Keyboard.KEY_DOWN:
                if (selectedCheatIndex != -1) {
                	selectedCheatIndex++;
                	if (selectedCheatIndex >= categoryTabs.get(selectedIndex)
            				.getCheatTabs().size()) {
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
                		selectedCheatIndex = categoryTabs.get(selectedIndex)
                				.getCheatTabs().size() - 1;
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
                if (selectedCheatIndex != -1) {
                    categoryTabs.get(selectedIndex)
                            .getCheatTabs()
                            .get(selectedCheatIndex)
                            .onInputKey(keyCode, true);
                    return;
                }
                
                selectedCheatIndex = 0;
                break;

            case Keyboard.KEY_LEFT:
                if (selectedCheatIndex == -1) return;
                selectedCheatIndex = -1;
                break;
        }
    }
}
