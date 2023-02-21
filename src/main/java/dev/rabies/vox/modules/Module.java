package dev.rabies.vox.modules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.config.Serializable;
import dev.rabies.vox.settings.BaseSetting;
import dev.rabies.vox.settings.KeyBinding;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

import java.util.LinkedList;
import java.util.Map;

@Getter
public class Module implements Serializable {
    protected final String name;
    private final KeyBinding bind;
    private final Category category;

    protected boolean toggled;
    protected Object suffix;

    protected final Minecraft mc = Minecraft.getMinecraft();
    private final LinkedList<BaseSetting<?>> settings = new LinkedList<>();

    public Module(String name, Category category) {
        this(name, category, KeyBinding.none());
    }

    public Module(String name, Category category, KeyBinding bind) {
        this.name = name;
        this.category = category;
        this.bind = bind;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void toggle() {
        toggled = !toggled;

        if (toggled) {
            onEnable();
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
            onDisable();
        }
    }

    protected <T extends BaseSetting<?>> T registerSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    protected void sendPacket(Packet<?> packetIn) {
        mc.player.connection.sendPacket(packetIn);
    }

    protected void sendPacketNoEvent(Packet<?> packetIn) {
        VoxMod.setIgnorePacket(true);
        mc.player.connection.sendPacket(packetIn);
    }

    protected boolean isToggleable() {
        if (mc.player == null || mc.world == null) {
            MinecraftForge.EVENT_BUS.unregister(this);
            toggled = false;
            return true;
        }
        return false;
    }

    @Override
    public JsonElement serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("toggled", toggled);
        jsonObject.addProperty("bind", bind.getKeyCode());
        jsonObject.addProperty("type", bind.getType().name());

        for (BaseSetting<?> setting : settings) {
            jsonObject.add(setting.getName(), setting.serialize());
        }

        return jsonObject;
    }

    @Override
    public void deserialize(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            bind.setKeyCode(jsonObject.get("bind").getAsInt());
            bind.setType(KeyBinding.BindType.valueOf(jsonObject.get("type").getAsString()));
            if ((jsonObject.get("toggled").getAsBoolean() && !toggled) || (!jsonObject.get("toggled").getAsBoolean() && toggled)) {
                toggle();
            }

            for (Map.Entry<String, JsonElement> elementEntry : jsonObject.entrySet()) {
                for (BaseSetting<?> setting : settings) {
                    if (setting.getName().equals(elementEntry.getKey())) {
                        setting.deserialize(elementEntry.getValue());
                        break;
                    }
                }
            }
        }
    }
}
