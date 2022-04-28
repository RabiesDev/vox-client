package dev.rabies.vox.commands;

import lombok.Getter;
import net.minecraft.client.Minecraft;

public abstract class Command {

    protected final Minecraft mc;

    @Getter private final String name;
    @Getter private final String[] aliases;

    public Command(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.mc = Minecraft.getMinecraft();
    }

    public abstract void execute(String[] args);
}
