package dev.rabies.vox.config;

import com.google.gson.*;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.Setting;
import dev.rabies.vox.utils.ChatUtils;
import dev.rabies.vox.utils.ModFile;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    @Getter
    private final ModFile configFolder = new ModFile(new File(VoxMod.getModFolder().getFile(), "configs"), true);
    @Getter
    private final List<Config> configs = new ArrayList<>();

    public ConfigManager() {
        reloadConfigs();
    }

    public void loadConfig(String name) {
        Config config = configs.stream().filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
        if (config == null) {
            if (Minecraft.getMinecraft().world == null) {
                System.out.println("No config found");
            } else {
                ChatUtils.error("No config found");
            }
            return;
        }

        JsonObject jsonObject = config.getJsonObject();
        for (Map.Entry<String, JsonElement> cheatEntry : jsonObject.entrySet()) {
            Cheat cheat = VoxMod.get().getCheatByName(cheatEntry.getKey());
            if (cheat == null) continue;
            JsonObject cheatObject = cheatEntry.getValue().getAsJsonObject();

            boolean active = cheatObject.getAsJsonPrimitive("active").getAsBoolean();
            if (cheat.isEnabled() && !active) {
                cheat.toggle();
            } else if (!cheat.isEnabled() && active) {
                cheat.toggle();
            }

            JsonObject keyBindObject = cheatObject.getAsJsonObject("keyBind");
            int keyCode = keyBindObject.getAsJsonPrimitive("keyCode").getAsInt();
            String bindType = keyBindObject.getAsJsonPrimitive("bindType").getAsString();
            cheat.getBind().update(keyCode, bindType);

            JsonObject settingsObject = cheatObject.getAsJsonObject("settings");
            for (Map.Entry<String, JsonElement> settingEntry : settingsObject.entrySet()) {
                Setting<?> setting = cheat.getSettingByName(settingEntry.getKey());
                if (setting == null) continue;
                if (setting instanceof BoolSetting) {
                    Setting<Boolean> boolSetting = (Setting<Boolean>) setting;
                    boolSetting.setValue(settingEntry.getValue().getAsBoolean());
                }
            }
        }
    }

    public void saveConfig(String name, String author) {
        File newConfig = new File(configFolder.getFile(), String.format("%s.json", name));
        if (newConfig.exists()) return;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("author", author);

        for (Cheat cheat : VoxMod.get().getCheats()) {
            JsonObject settingsObject = new JsonObject();
            for (Setting<?> setting : cheat.getSettings()) {
                if (setting instanceof BoolSetting) {
                    Setting<Boolean> boolSetting = (Setting<Boolean>) setting;
                    settingsObject.addProperty(setting.getLabel(), boolSetting.getValue());
                }
            }

            JsonObject keyBindObject = new JsonObject();
            keyBindObject.addProperty("keyCode", cheat.getBind().getKeyCode());
            keyBindObject.addProperty("bindType", cheat.getBind().getType().name());

            JsonObject cheatObject = new JsonObject();
            cheatObject.addProperty("active", cheat.isEnabled());
            cheatObject.add("keyBind", keyBindObject);
            cheatObject.add("settings", settingsObject);
            jsonObject.add(cheat.getName(), cheatObject);
        }

        String result = "";
        try {
            FileUtils.writeStringToFile(newConfig, gson.toJson(jsonObject), StandardCharsets.UTF_8);
            configs.add(new Config(name, author, jsonObject));
            result = "Config saved";
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        
        if (Minecraft.getMinecraft().world != null) {
            ChatUtils.info(result);
        }
    }

    public void reloadConfigs() {
        File folder = configFolder.getFile();
        File[] configFiles = folder.listFiles(it -> !it.isDirectory() && FilenameUtils.getExtension(it.getName()).equals("json"));
        if (configFiles == null) {
            if (Minecraft.getMinecraft().world == null) {
                System.out.println("No config found");
            } else {
                ChatUtils.error("No config found");
            }
            return;
        }

        configs.clear();
        for (File file : configFiles) {
            try {
                String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
                String name = jsonObject.getAsJsonPrimitive("name").getAsString();
                String author = jsonObject.getAsJsonPrimitive("author").getAsString();
                configs.add(new Config(name, author, jsonObject));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Minecraft.getMinecraft().world != null) {
            ChatUtils.info("Config has been reloaded");
        }
    }
}
