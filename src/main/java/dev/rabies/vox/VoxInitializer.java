package dev.rabies.vox;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.cheats.*;
import dev.rabies.vox.commands.*;
import dev.rabies.vox.config.ConfigManager;
import dev.rabies.vox.render.RenderHook;
import dev.rabies.vox.render.font.FontData;
import dev.rabies.vox.render.font.SystemFontRenderer;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VoxInitializer implements Initializer {

    @Getter private final ArrayList<FontData> fontCaches = new ArrayList<>();
    @Getter private final ArrayList<Cheat> cheats = new ArrayList<>();
    @Getter private final ArrayList<Command> commands = new ArrayList<>();
    @Getter
    private ConfigManager configManager;
    @Getter
    private boolean debugMode;

    @Override
    public void preInitialize(FMLPreInitializationEvent event) {
        if (System.getProperty("vox") != null) {
            debugMode = System.getProperty("vox").equalsIgnoreCase("debug");
        }
    }

    @Override
    public void initialize(FMLInitializationEvent event) {
        registerCheats();
        registerCommands();
    }

    @Override
    public void postInitialize(FMLPostInitializationEvent event) {
        configManager = new ConfigManager();
        configManager.loadConfig("default");
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new RenderHook());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            configManager.saveConfig("default", Constants.MOD_NAME, true);
        }));
    }

    private void registerCheats() {
        Collections.addAll(cheats,
                new DebugCheat(),
                new HudCheat(),
                new AutoSprintCheat(),
                new AutoClickerCheat(),
                new InvPlusCheat(),
                new ESPCheat(),
                new WTapCheat(),
                new AutoToolCheat()
        );
    }

    private void registerCommands() {
        Collections.addAll(commands,
                new HelpCommand(),
                new ToggleCommand(),
                new BindCommand(),
                new ConfigCommand()
        );
    }

    public Cheat getCheatByName(String name) {
        return cheats.stream().filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public List<Cheat> getCheatsByCategory(Category category) {
        return cheats.stream().filter(it -> it.getCategory() == category)
                .collect(Collectors.toList());
    }

    public SystemFontRenderer newSystemFont(String fontName, int fontSize) {
        FontData cache = fontCaches.stream().filter(it -> it.getName().equalsIgnoreCase(fontName) && it.getSize() == fontSize)
                .findFirst().orElse(null);
        if (cache != null) return cache.getRenderer();

        Font myFont = null;
        for (String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (!s.equals(fontName)) continue;
            myFont = new Font(fontName, Font.PLAIN, fontSize * 2);
            break;
        }

        if (myFont == null) {
            try (InputStream inputStream = this.getClass().getResourceAsStream(String.format("/assets/minecraft/vox/%s.ttf", fontName))) {
                if (inputStream == null) return null;
                myFont = Font.createFont(0, inputStream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) fontSize);
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
                return null;
            }
        }

        SystemFontRenderer sfr = new SystemFontRenderer(myFont);
        fontCaches.add(new FontData(fontName, fontSize, sfr));
        return sfr;
    }
}
