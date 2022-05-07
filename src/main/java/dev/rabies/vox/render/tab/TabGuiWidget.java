package dev.rabies.vox.render.tab;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.render.Widget;
import dev.rabies.vox.render.font.SystemFontRenderer;
import dev.rabies.vox.utils.DrawUtils;
import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabGuiWidget extends Widget implements TabActionListener {

    private final SystemFontRenderer labelFont = VoxMod.get().newSystemFont("Mukta-Regular", 20);
    @Getter
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
        int offset = 6;
        double height = ((labelFont.getHeight() / 1.3) + offset - 1) * categoryTabs.size();
        double width = 0;
        for (CategoryTab tab : categoryTabs) {
            if (labelFont.getStringWidth(tab.getLabel()) > width) {
                width = labelFont.getStringWidth(tab.getLabel());
            }
        }
        width += offset * 3;

        int bg = new Color(20, 20, 20, 200).getRGB();
        int theme = new Color(110, 255, 60).getRGB();

        GlStateManager.translate(5, 20, 0);
        DrawUtils.drawRect(0, 0, width, height, bg);
        GlStateManager.glLineWidth(1.2F);
        DrawUtils.drawRect(GL11.GL_LINE_LOOP, 0, 0, width, height, theme);

        int subOffset = offset - 1;
        int offsetY = subOffset - 1;
        for (int i = 0; i < categoryTabs.size(); i++) {
            CategoryTab tab = categoryTabs.get(i);
            Color col = new Color(255, 255, 255).darker();
            if (selectedIndex == i) {
                col = col.brighter().brighter();

                if (selectedCheatIndex != -1) {
                	col = new Color(110, 255, 60);
                    tab.renderCheatsTab(width + 3, offsetY - 4, selectedCheatIndex);
                }
            }

            labelFont.drawStringWithShadow(tab.getLabel(), subOffset, offsetY - 2, col.getRGB());
            offsetY += labelFont.getHeight() / 1.3;
            offsetY += subOffset;
        }

        GlStateManager.translate(-5, -20, 0);
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
            	CategoryTab tab = categoryTabs.get(selectedIndex);
                if (selectedCheatIndex != -1) {
                	tab.getCheatTabs().get(selectedCheatIndex)
                            .onInputKey(keyCode, true);
                    return;
                }
                
                if (tab.getCheatTabs().isEmpty()) return;
                selectedCheatIndex = 0;
                break;

            case Keyboard.KEY_LEFT:
                if (selectedCheatIndex == -1) return;
                selectedCheatIndex = -1;
                break;
        }
    }
}
