package dev.rabies.vox.config;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

public class Config {

    @Getter
    private final String name;
    @Getter
    private final String author;
    @Getter @Setter
    private JsonObject jsonObject;

    public Config(String name, String author, JsonObject jsonObject) {
        this.name = name;
        this.author = author;
        this.jsonObject = jsonObject;
    }
}
