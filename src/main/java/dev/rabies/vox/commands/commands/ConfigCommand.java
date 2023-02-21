package dev.rabies.vox.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.rabies.vox.commands.Command;
import net.minecraft.command.ICommandSender;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config", "cfg");
    }

    @Override
    public void build(LiteralArgumentBuilder<ICommandSender> builder) {
        builder.then(literal("name").then(literal("action").executes(context -> {
            switch (context.getArgument("action", String.class)) {
                case "save":
                    break;

                case "load":
                    break;
            }
            return 1;
        })));
    }
}
