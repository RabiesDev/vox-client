package dev.rabies.vox.config;

import com.google.gson.JsonElement;

public interface Serializable {
    JsonElement serialize();

    void deserialize(JsonElement element);
}
