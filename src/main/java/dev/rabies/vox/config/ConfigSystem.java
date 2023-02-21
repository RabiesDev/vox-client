package dev.rabies.vox.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.modules.ModuleRegistry;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class ConfigSystem {
    @Getter
    private static final File configFolder = new File(VoxMod.getClientFolder(), "configs");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void serialize(String name, boolean overwrite) {
        try {
            if (!configFolder.exists()) {
                configFolder.createNewFile();
            }

            File configFile = new File(configFolder, name + ".json");
            if (configFile.exists() && !overwrite) {
                return;
            } else if (!configFile.exists()) {
                configFile.createNewFile();
            }

            JsonObject jsonObject = new JsonObject();
            for (Module module : ModuleRegistry.getModules()) {
                jsonObject.add(module.getName(), module.serialize());
            }

            FileUtils.writeStringToFile(configFile, gson.toJson(jsonObject), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserialize(String name) {
        try {
            if (!configFolder.exists()) {
                configFolder.mkdir();
            }

            for (File configFile : Objects.requireNonNull(configFolder.listFiles(it -> FilenameUtils.getExtension(it.getName()).equalsIgnoreCase("json")))) {
                if (configFile.getName().split("\\.")[0].equals(name)) {
                    JsonObject jsonObject = gson.fromJson(FileUtils.readFileToString(configFile, StandardCharsets.UTF_8), JsonObject.class);
                    for (Map.Entry<String, JsonElement> elementEntry : jsonObject.entrySet()) {
                        Module module = ModuleRegistry.fromString(elementEntry.getKey().replace(" ", ""));
                        if (module != null) {
                            module.deserialize(elementEntry.getValue());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
