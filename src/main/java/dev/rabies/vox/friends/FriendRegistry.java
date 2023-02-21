package dev.rabies.vox.friends;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.rabies.vox.VoxMod;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FriendRegistry {
    @Getter
    private static final File friendFile = new File(VoxMod.getClientFolder(), "friends.json");
    @Getter
    private static final List<String> friends = new CopyOnWriteArrayList<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void serialize() {
        try {
            if (!friendFile.exists()) {
                friendFile.createNewFile();
            }

            JsonArray jsonArray = new JsonArray();
            for (String ign : friends) {
                jsonArray.add(ign);
            }

            FileUtils.writeStringToFile(friendFile, gson.toJson(jsonArray), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserialize() {
        try {
            if (!friendFile.exists()) {
                friendFile.createNewFile();
                return;
            }

            JsonArray jsonArray = gson.fromJson(FileUtils.readFileToString(friendFile, StandardCharsets.UTF_8), JsonArray.class);
            if (jsonArray != null && jsonArray.size() > 0) {
                for (JsonElement element : jsonArray) {
                    friends.add(element.getAsString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
