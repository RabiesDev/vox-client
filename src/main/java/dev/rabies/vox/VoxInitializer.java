package dev.rabies.vox;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.cheats.*;
import dev.rabies.vox.commands.*;
import dev.rabies.vox.config.ConfigManager;
import dev.rabies.vox.friend.FriendManager;
import dev.rabies.vox.render.RenderHook;
import dev.rabies.vox.render.font.FontData;
import dev.rabies.vox.render.font.SystemFontRenderer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
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
    @Getter private final ArrayList<CheatWrapper> cheats = new ArrayList<>();
    @Getter private final ArrayList<Command> commands = new ArrayList<>();
    @Getter
    private ConfigManager configManager;
    @Getter
    private FriendManager friendManager;
    @Getter @Setter
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
        friendManager = new FriendManager();
        friendManager.loadFriends();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new RenderHook());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (getCheatByName("brightness").isEnabled()) {
                BrightnessCheat brightnessCheat = (BrightnessCheat) getCheatByName("brightness");
                Minecraft.getMinecraft().gameSettings.gammaSetting = brightnessCheat.getPrevGammaSetting();
            }
            configManager.saveConfig("default", Constants.MOD_NAME, true);
            friendManager.saveFriends();
        }));
    }

    private void registerCheats() {
        Collections.addAll(cheats,
                new DebugCheat(),
                new HudCheat(),
                new AutoSprintCheat(),
                new AutoClickerCheat(),
                new InvPlusCheat(),
                new EspCheat(),
                new WTapCheat(),
                new AutoToolCheat(),
                new AntiForgeBypassCheat(),
                new ReachCheat(),
                new ChamsCheat(),
                new SneakCheat(),
                new AutoRespawnCheat(),
                new FreecamCheat(),
                new OutlineEspCheat(),
                new HitBoxCheat(),
                new FakeFpsCheat(),
                new TabGuiCheat(),
                new StreamerCheat(),
                new AimAssistCheat(),
                new PackSpooferCheat(),
                new PingSpooferCheat(),
                new BrightnessCheat(),
                new VelocityCheat()
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

    public CheatWrapper getCheatByName(String name) {
        return cheats.stream().filter(it -> it.getName().replace(" ", "")
                        .equalsIgnoreCase(name.replace(" ", "")))
                .findFirst().orElse(null);
    }

    public List<CheatWrapper> getCheatsByCategory(Category category) {
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
