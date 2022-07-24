package dev.rabies.vox.commands;

import lombok.Getter;
import net.minecraft.client.Minecraft;

public abstract class Command {

    protected final Minecraft mc = Minecraft.getMinecraft();
    @Getter private final String[] aliases;

    public Command(String... aliases) {
        this.aliases = aliases;
    }

    public abstract void execute(String[] args);
}
