package dev.rabies.vox.friend;

import com.google.gson.*;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.utils.misc.ModFile;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {

    @Getter
    private final ModFile friendFile = new ModFile(new File(VoxMod.getModFolder().getFile(), "friends.json"), false);
    @Getter
    private final List<Friend> friends = new ArrayList<>();
    @Getter @Setter
    private JsonArray friendsJsonArray;

    public void loadFriends() {
        if (!friendFile.exists()) return;
        try {
            String content = FileUtils.readFileToString(friendFile.getFile(), StandardCharsets.UTF_8);
            if (content.isEmpty()) return;
            JsonArray jsonArray = new JsonParser().parse(content).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                if (!(jsonElement instanceof JsonObject)) continue;
                JsonObject friendObject = (JsonObject) jsonElement;
                friends.add(new Friend(
                        friendObject.getAsJsonPrimitive("Ign").getAsString(),
                        friendObject.getAsJsonPrimitive("Nick").getAsString()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFriends() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (Friend friend : friends) {
            JsonObject friendObject = new JsonObject();
            friendObject.addProperty("Ign", friend.getIgn());
            friendObject.addProperty("Nick", friend.getNick());
            friendsJsonArray.add(friendObject);
        }

        try {
            FileUtils.writeStringToFile(friendFile.getFile(), gson.toJson(friendsJsonArray), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
