package dev.rabies.vox.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.rabies.vox.commands.Command;
import dev.rabies.vox.commands.arguments.ModuleArgumentType;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.utils.misc.ChatHelper;
import net.minecraft.command.ICommandSender;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle", "t");
    }

    @Override
    public void build(LiteralArgumentBuilder<ICommandSender> builder) {
        builder.then(argument("module", new ModuleArgumentType()).executes(context -> {
            Module module = context.getArgument("module", Module.class);
            module.toggle();
            ChatHelper.info(String.format("%s has been %s", module.getName(), module.isToggled() ? "\247aenabled" : "\247cdisabled"));
            return 1;
        }));
    }
}
