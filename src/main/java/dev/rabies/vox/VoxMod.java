package dev.rabies.vox;

import dev.rabies.vox.commands.CommandRegistry;
import dev.rabies.vox.config.ConfigSystem;
import dev.rabies.vox.friends.FriendRegistry;
import dev.rabies.vox.modules.ModuleRegistry;
import dev.rabies.vox.modules.modules.BrightnessModule;
import dev.rabies.vox.render.RenderHook;
import dev.rabies.vox.render.font.FontData;
import dev.rabies.vox.render.font.SystemFontRenderer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class VoxMod {
    @Getter
    private static final File clientFolder = new File(Constants.MOD_NAME);
    @Getter
    private static final List<FontData> fontCaches = new ArrayList<>();
    @Getter
    private static boolean debugMode = false;
    @Getter @Setter
    private static boolean ignorePacket = false;

    @Mod.EventHandler
    public void onPreInitialize(FMLPreInitializationEvent event) {
        if (!clientFolder.exists()) {
            clientFolder.mkdir();
        }

        if (System.getProperty("vox") != null) {
            debugMode = System.getProperty("vox").equalsIgnoreCase("debug");
        }
    }

    @Mod.EventHandler
    public void onInitialize(FMLInitializationEvent event) {
        ModuleRegistry.initialize();
        CommandRegistry.initialize();

        FriendRegistry.deserialize();
        ConfigSystem.deserialize("default");

        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new RenderHook());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            BrightnessModule brightnessModule = ModuleRegistry.fromInstance(BrightnessModule.class);
            if (brightnessModule.isToggled()) {
                Minecraft.getMinecraft().gameSettings.gammaSetting = brightnessModule.getLastGamma();
            }

            FriendRegistry.serialize();
            ConfigSystem.serialize("default", true);
        }));
    }

    public static SystemFontRenderer newSystemFont(String fontName, int fontSize) {
        FontData cache = fontCaches.stream().filter(it -> it.getName().equalsIgnoreCase(fontName) && it.getSize() == fontSize).findFirst().orElse(null);
        if (cache != null) {
            return cache.getRenderer();
        }

        Font myFont = null;
        for (String availableFontFName : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (availableFontFName.equals(fontName)) {
                myFont = new Font(availableFontFName, Font.PLAIN, fontSize * 2);
                break;
            }
        }

        if (myFont == null) {
            try (InputStream inputStream = VoxMod.class.getResourceAsStream(String.format("/assets/minecraft/vox/font/%s.ttf", fontName))) {
                if (inputStream != null) {
                    myFont = Font.createFont(0, inputStream);
                    myFont = myFont.deriveFont(Font.PLAIN, (float) fontSize);
                }
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
                return null;
            }
        }

        SystemFontRenderer font = new SystemFontRenderer(myFont);
        fontCaches.add(new FontData(fontName, fontSize, font));
        return font;
    }
}
