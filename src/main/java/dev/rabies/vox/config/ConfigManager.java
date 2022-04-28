package dev.rabies.vox.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.utils.ModFile;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    @Getter
    private final ModFile configFolder = new ModFile(new File(VoxMod.getModFolder().getFile(), "configs"), true);
    @Getter
    private final List<Config> configs = new ArrayList<>();

    public ConfigManager() {
        reloadConfigs();
    }

    public void loadConfig() {

    }

    public void saveConfig() {

    }

    public void reloadConfigs() {
        File folder = configFolder.getFile();
        File[] configFiles = folder.listFiles(it -> !it.isDirectory() && FilenameUtils.getExtension(it.getName()).equals("json"));
        if (configFiles == null) {
            System.out.println("No config found");
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
    }
}
