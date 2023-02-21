package dev.rabies.vox.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;

public abstract class Command {
    @Getter
    private final String[] aliases;

    public Command(String... aliases) {
        this.aliases = aliases;
    }

    public abstract void build(LiteralArgumentBuilder<ICommandSender> builder);

    public void registerTo(CommandDispatcher<ICommandSender> dispatcher) {
        for (String alias : aliases) {
            LiteralArgumentBuilder<ICommandSender> builder = LiteralArgumentBuilder.literal(alias);
            build(builder);
            dispatcher.register(builder);
        }
    }

    protected <T> RequiredArgumentBuilder<ICommandSender, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    protected LiteralArgumentBuilder<ICommandSender> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }
}
