package dev.rabies.vox.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.rabies.vox.commands.arguments.KeyArgumentType;
import dev.rabies.vox.commands.arguments.ModuleArgumentType;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.commands.Command;
import net.minecraft.command.ICommandSender;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind", "b");
    }

    @Override
    public void build(LiteralArgumentBuilder<ICommandSender> builder) {
        builder.then(argument("module", new ModuleArgumentType()).then(argument("key", new KeyArgumentType())).executes(context -> {
            context.getArgument("module", Module.class).getBind().setKeyCode(context.getArgument("key", Integer.class));
            return 1;
        }));
    }
}
